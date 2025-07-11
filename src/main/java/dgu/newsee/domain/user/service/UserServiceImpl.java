package dgu.newsee.domain.user.service;

import dgu.newsee.domain.user.converter.UserConverter;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.UserTokenResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.UserAuthResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.LevelResponse;
import dgu.newsee.domain.user.dto.UserProfile;
import dgu.newsee.domain.user.entity.Level;
import dgu.newsee.domain.user.entity.User;
import dgu.newsee.domain.user.repository.UserRepository;
import dgu.newsee.global.exception.UserException;
import dgu.newsee.global.payload.ResponseCode;
import dgu.newsee.global.security.JwtTokenProvider;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.ProfileUpdateResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserRequest.ProfileUpdateRequest;
import dgu.newsee.domain.user.dto.UserDTO.UserRequest.LevelRequest;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.UserInfoResponse;
import dgu.newsee.domain.words.repository.SavedWordRepository;
import dgu.newsee.global.security.JwtUtil;
import dgu.newsee.global.security.OAuthUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuthUtil oAuthUtil;
    private final JwtUtil jwtUtil;
    private final SavedWordRepository savedWordRepository;

    @Override
    public UserAuthResponse kakaoLogin(String accessTokenFromKakao) {
        // 1. OAuth 서버에서 사용자 정보 조회
        UserProfile profile = oAuthUtil.getKakaoProfile(accessTokenFromKakao);

        // 2. DB 조회
        Optional<User> existingUser = userRepository.findByEmail(profile.getEmail());
        boolean isNew = false;

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = userRepository.save(UserConverter.toUser(profile, "KAKAO"));
            isNew = true;
        }

        // 3. 토큰 발급
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        user.updateRefreshToken(refreshToken); // DB 저장용

        // 4. 응답 변환
        return UserConverter.toUserAuthResponse(user, accessToken, refreshToken, isNew);
    }

    @Override
    public UserTokenResponse refreshAccessToken(String refreshToken) {
        Long extractedUserId = jwtUtil.extractUserId(refreshToken);
        if (extractedUserId == null) {
            throw new UserException(ResponseCode.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findById(extractedUserId)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new UserException(ResponseCode.INVALID_REFRESH_TOKEN);
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(user);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);
        user.updateRefreshToken(newRefreshToken);

        return UserConverter.toUserTokenResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public ProfileUpdateResponse updateProfile(String userId, ProfileUpdateRequest request) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));

        if (request.getName() != null) user.updateName(request.getName());

        return UserConverter.toProfileUpdateResponse(user);
    }

    @Override
    public LevelResponse registerLevel(String userId, LevelRequest request) {
        if (userId == null) {
            // 비로그인 사용자
            return LevelResponse.builder()
                    .userId(null)
                    .name(null)
                    .level(Level.MEDIUM) // 무조건 중으로 설정
                    .build();
        }

        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));

        Level level = parseLevel(request.getLevel());
        user.updateLevel(level);
        userRepository.save(user);

        return UserConverter.toLevelResponse(user);
    }

    @Override
    public LevelResponse updateLevel(String userId, LevelRequest request) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));

        Level level = parseLevel(request.getLevel());
        user.updateLevel(level);
        userRepository.save(user);

        return UserConverter.toLevelResponse(user);

    }

    private Level parseLevel(String levelString) {
        if (levelString == null) return Level.MEDIUM;
        return switch (levelString) {
            case "상" -> Level.HIGH;
            case "중" -> Level.MEDIUM;
            case "하" -> Level.LOW;
            default -> Level.MEDIUM;
        };
    }

    @Override
    public UserInfoResponse getUserInfo(String userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));

        int savedWordCount = savedWordRepository.findByUserId(user.getUserId()).size();

        return UserConverter.toUserInfoResponse(user, savedWordCount);
    }
}