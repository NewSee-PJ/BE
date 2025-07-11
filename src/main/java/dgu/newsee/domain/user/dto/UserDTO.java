package dgu.newsee.domain.user.dto;

import dgu.newsee.domain.user.entity.Level;
import dgu.newsee.global.security.OAuthProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDTO {
    public static class UserRequest {
        @Getter
        @NoArgsConstructor
        public static class SocialLoginRequest {
            private String accessToken;
        }

        @Getter
        @NoArgsConstructor
        public static class ProfileUpdateRequest {
            private String name;
        }

        @Getter
        @NoArgsConstructor
        public static class LevelRequest {
            private String level;
        }

    }

    public static class UserResponse {

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class UserAuthResponse {
            private String provider;
            private String email;
            private String name;
            private String profileImage;
            private String accessToken;
            private String refreshToken;
            private String level;
            private boolean isNew; // 신규 가입 여부
        }

        @Builder
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class UserTokenResponse {
            @Schema(description = "Access Token", example = "eyJhbGciOiJIUzI1NiIsInR...")
            private String accessToken;

            @Schema(description = "Refresh Token", example = "eyJhbGciOiJIUzI1NiIsInR...")
            private String refreshToken;
        }

        @Getter
        @Builder
        @AllArgsConstructor
        public static class ProfileUpdateResponse {
            private Long userId;
            private String email;
            private String name;
            private OAuthProvider provider;
            private String profileImage;
            private Level level;
        }

        @Getter
        @Builder
        @AllArgsConstructor
        public static class LevelResponse {
            private Long userId;
            private String name;
            private Level level;
        }


        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class UserInfoResponse {
            private Long userId;
            private String name;
            private String profileImage;
            private Level level;
            private String joinDate;
            private int savedWordCount;
        }
    }
}
