package dgu.newsee.domain.user.entity;

import dgu.newsee.global.common.BaseEntity;
import dgu.newsee.global.security.OAuthProvider;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    private String email;
    private LocalDate joinDate;
    private Level level;
    private String profileImage;
    @Enumerated(EnumType.STRING)
    private OAuthProvider provider;
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;
    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
