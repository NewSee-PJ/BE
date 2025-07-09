package dgu.newsee.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfile {
    private String email;
    private String name;
    private String profileImage;
}
