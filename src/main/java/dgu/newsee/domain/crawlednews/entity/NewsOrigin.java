package dgu.newsee.domain.crawlednews.entity;
import dgu.newsee.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NewsOrigin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    private String category;

    private String source;

    private LocalDateTime time;

    private String originalUrl;

    @Enumerated(EnumType.STRING) // DB에는 USER_INPUT, AUTO_CRAWLED로 저장됨
    private NewsStatus status;
}
