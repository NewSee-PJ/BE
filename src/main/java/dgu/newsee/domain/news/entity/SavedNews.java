package dgu.newsee.domain.news.entity;

import dgu.newsee.domain.news.entity.News;
import dgu.newsee.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@IdClass(SavedNewsId.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedNews {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    private LocalDateTime savedAt;

    @PrePersist
    protected void onCreate() {
        this.savedAt = LocalDateTime.now();
    }
}