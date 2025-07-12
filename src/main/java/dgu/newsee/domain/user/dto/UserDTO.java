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
            @Schema(description = "카카오 Access Token", example = "MUXOVHyvdMq7mNGzxOIc7g1J2dSJTXh5AAAAAQoXAQ8AAAGX-SMt...")
            private String accessToken;
        }

        @Getter
        @NoArgsConstructor
        public static class ProfileUpdateRequest {
            @Schema(description = "변경할 사용자 이름", example = "서하봉")
            private String name;
        }

        @Getter
        @NoArgsConstructor
        public static class LevelRequest {
            @Schema(description = "문해력 수준 (상/중/하)", example = "중")
            private String level;
        }

    }

    public static class UserResponse {

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class UserAuthResponse {
            @Schema(description = "OAuth Provider", example = "KAKAO")
            private String provider;

            @Schema(description = "사용자 이메일", example = "example@kakao.com")
            private String email;

            @Schema(description = "사용자 이름", example = "홍길동")
            private String name;

            @Schema(description = "프로필 이미지 URL", example = "https://kakao.com/profile.jpg")
            private String profileImage;

            @Schema(description = "Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
            private String accessToken;

            @Schema(description = "Refresh Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
            private String refreshToken;

            @Schema(description = "문해력 수준 (HIGH/MEDIUM/LOW)", example = "MEDIUM")
            private String level;

            @Schema(description = "신규 가입 여부", example = "true")
            private boolean isNew;
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
            @Schema(description = "사용자 ID", example = "1")
            private Long userId;

            @Schema(description = "이메일", example = "example@kakao.com")
            private String email;

            @Schema(description = "이름", example = "홍길동")
            private String name;

            @Schema(description = "OAuth 제공자", example = "KAKAO")
            private OAuthProvider provider;

            @Schema(description = "프로필 이미지 URL", example = "https://kakao.com/profile.jpg")
            private String profileImage;

            @Schema(description = "문해력 레벨", example = "MEDIUM")
            private Level level;
        }

        @Getter
        @Builder
        @AllArgsConstructor
        public static class LevelResponse {
            @Schema(description = "사용자 ID", example = "1")
            private Long userId;

            @Schema(description = "이름", example = "홍길동")
            private String name;

            @Schema(description = "문해력 레벨", example = "LOW")
            private Level level;
        }


        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class UserInfoResponse {
            @Schema(description = "사용자 ID", example = "1")
            private Long userId;

            @Schema(description = "이름", example = "서하봉")
            private String name;

            @Schema(description = "프로필 이미지", example = "https://kakao.com/profile.jpg")
            private String profileImage;

            @Schema(description = "문해력 레벨", example = "HIGH")
            private Level level;

            @Schema(description = "가입일", example = "2025-07-11")
            private String joinDate;

            @Schema(description = "저장한 단어 수", example = "15")
            private int savedWordCount;
        }
    }
}
