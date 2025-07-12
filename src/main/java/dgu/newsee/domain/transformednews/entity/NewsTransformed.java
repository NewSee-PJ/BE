package dgu.newsee.domain.transformednews.entity;

import dgu.newsee.domain.crawlednews.entity.NewsOrigin;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NewsTransformed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private dgu.newsee.domain.crawlednews.entity.NewsStatus status; // 사용자/시스템 구분

    @Column(name = "level", length = 10)
    @Enumerated(EnumType.STRING)
    private TransformLevel level;



    @Lob
    private String transformedContent;

    private String summarized;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private NewsOrigin news; //
}
