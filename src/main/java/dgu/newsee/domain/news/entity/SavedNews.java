package dgu.newsee.domain.news.entity;

import dgu.newsee.domain.crawlednews.entity.NewsOrigin;
import dgu.newsee.domain.transformednews.entity.TransformLevel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SavedNews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private TransformLevel savedLevel; // 북마크 당시 level로 저장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_origin_id")
    private NewsOrigin newsOrigin;
}