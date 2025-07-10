package dgu.newsee.domain.news.repository;

import dgu.newsee.domain.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
    boolean existsByOriginalUrl(String url);
}
