package dgu.newsee.domain.user.dto;

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
    }
}
