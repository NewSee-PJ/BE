package dgu.newsee.domain.words.entity;

import dgu.newsee.domain.crawlednews.entity.NewsOrigin;
import dgu.newsee.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "words")
public class Word extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private NewsOrigin news;

    private String term;

    @Column(length = 2000)
    private String description;

    private String category;
}