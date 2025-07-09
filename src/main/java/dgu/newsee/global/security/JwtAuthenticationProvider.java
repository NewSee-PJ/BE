package dgu.newsee.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;

        Long userId = (Long) token.getPrincipal();
        String role = (String) token.getCredentials();

        if (userId == null || role == null) {
            throw new BadCredentialsException("Missing authentication info.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUserId(userId);

        if (!userDetails.getRole().equals(role)) {
            throw new BadCredentialsException("Role mismatch.");
        }

        return new JwtAuthenticationToken(userDetails, role, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
