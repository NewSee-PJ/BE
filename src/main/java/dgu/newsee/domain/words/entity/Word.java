package dgu.newsee.domain.words.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "words")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordId;

    private Integer newsId;

    private String term;

    @Column(length = 2000)
    private String description;

    private String category;
}