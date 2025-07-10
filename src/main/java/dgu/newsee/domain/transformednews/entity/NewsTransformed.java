package dgu.newsee.domain.transformednews.entity;

import dgu.newsee.domain.news.entity.News;
import dgu.newsee.domain.words.entity.Word;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NewsTransformed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String level;

    @Column(length = 5000)
    private String transformedContent;

    @Column(length = 1000)
    private String summarized;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    @OneToMany(mappedBy = "newsTransformed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Word> difficultWords;
}
