package dgu.newsee.domain.user.converter;

import dgu.newsee.domain.user.dto.UserDTO.UserResponse.ProfileUpdateResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.UserAuthResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.LevelResponse;
import dgu.newsee.domain.user.dto.UserDTO.UserResponse.UserInfoResponse;
import dgu.newsee.domain.user.dto.UserProfile;
import dgu.newsee.domain.user.entity.Level;
import dgu.newsee.domain.user.entity.Role;
import dgu.newsee.domain.user.entity.User;
import dgu.newsee.global.security.OAuthProvider;

import java.time.LocalDate;

public class UserConverter {
    public static User toUser(UserProfile profile, String provider) {
        return User.builder()
                .email(profile.getEmail())
                .name(profile.getName())
                .profileImage(profile.getProfileImage())
                .joinDate(LocalDate.now())
                .role(Role.ROLE_USER)
                .level(Level.MEDIUM)
                .provider(OAuthProvider.valueOf(provider.toUpperCase()))
                .build();
    }

    public static UserAuthResponse toUserAuthResponse(User user, String accessToken, String refreshToken, boolean isNew) {
        return new UserAuthResponse(
                user.getProvider().toString(),
                user.getEmail(),
                user.getName(),
                user.getProfileImage(),
                accessToken,
                refreshToken,
                user.getLevel().toString(),
                isNew
        );
    }

    public static ProfileUpdateResponse toProfileUpdateResponse(User user) {
        return ProfileUpdateResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .provider(user.getProvider())
                .profileImage(user.getProfileImage())
                .level(user.getLevel())
                .build();
    }

    public static LevelResponse toLevelResponse(User user) {
        return LevelResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .level(user.getLevel())
                .build();
    }

    public static UserInfoResponse toUserInfoResponse(User user, int savedWordCount) {
        return UserInfoResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .level(user.getLevel())
                .joinDate(user.getJoinDate().toString())
                .savedWordCount(savedWordCount)
                .build();
    }
}
