package dgu.newsee.domain.words.entity;

import dgu.newsee.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "saved_words")
public class SavedWord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long savedWordId;

    private Long userId;

    private Long wordId;

    private LocalDate date;
}