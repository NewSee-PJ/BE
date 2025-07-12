package dgu.newsee.domain.news.repository;

import dgu.newsee.domain.news.entity.NewsOrigin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<NewsOrigin, Long> {
    boolean existsByOriginalUrl(String url);
}
