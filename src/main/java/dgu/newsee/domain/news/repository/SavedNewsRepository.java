package dgu.newsee.domain.news.repository;

import dgu.newsee.domain.news.entity.SavedNews;
import dgu.newsee.domain.news.entity.SavedNewsId;
import dgu.newsee.domain.user.entity.User;
import dgu.newsee.domain.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedNewsRepository extends JpaRepository<SavedNews, SavedNewsId> {
    boolean existsByUserAndNews(User user, News news);
}
