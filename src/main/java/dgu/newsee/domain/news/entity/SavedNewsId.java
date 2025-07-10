package dgu.newsee.domain.news.entity;

import java.io.Serializable;
import java.util.Objects;

public class SavedNewsId implements Serializable {
    private Long user;
    private Long news;

    public SavedNewsId() {}

    public SavedNewsId(Long user, Long news) {
        this.user = user;
        this.news = news;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SavedNewsId that = (SavedNewsId) o;
        return Objects.equals(user, that.user) && Objects.equals(news, that.news);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, news);
    }
}