package dgu.newsee.global.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dgu.newsee.domain.user.dto.UserProfile;
import dgu.newsee.global.security.OAuthConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class OAuthUtil {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    public UserProfile getKakaoProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                OAuthConstants.KAKAO_PROFILE_URL,
                HttpMethod.GET,
                request,
                String.class
        );

        try {
            JsonNode json = objectMapper.readTree(response.getBody());
            JsonNode account = json.get("kakao_account");
            JsonNode profile = account.get("profile");

            return UserProfile.builder()
                    .email(account.get("email").asText())
                    .name(profile.get("nickname").asText())
                    .profileImage(profile.get("profile_image_url").asText())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("카카오 프로필 파싱 실패");
        }
    }
}
