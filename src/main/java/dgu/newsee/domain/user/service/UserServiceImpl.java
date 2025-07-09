package dgu.newsee.domain.user.service;

import dgu.newsee.domain.user.converter.UserConverter;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.UserAuthResponse;
import dgu.newsee.domain.user.dto.UserProfile;
import dgu.newsee.domain.user.entity.Role;
import dgu.newsee.domain.user.entity.User;
import dgu.newsee.domain.user.repository.UserRepository;
import dgu.newsee.global.security.JwtTokenProvider;
import dgu.newsee.global.security.OAuthProvider;
import dgu.newsee.global.security.OAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuthUtil oAuthUtil;

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
}