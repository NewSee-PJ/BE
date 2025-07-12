package dgu.newsee.domain.crawlednews.repository;

import dgu.newsee.domain.crawlednews.entity.NewsOrigin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<NewsOrigin, Long> {
    boolean existsByOriginalUrl(String url);
}
