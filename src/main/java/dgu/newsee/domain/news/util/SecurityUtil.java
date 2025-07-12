package dgu.newsee.domain.news.util;

import org.springframework.security.core.Authentication;

public class SecurityUtil {

    public static Long extractUserId(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        return Long.parseLong(authentication.getName());
    }
}
