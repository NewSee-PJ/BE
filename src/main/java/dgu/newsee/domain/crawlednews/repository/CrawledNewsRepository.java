package dgu.newsee.domain.crawlednews.repository;

import dgu.newsee.domain.crawlednews.entity.CrawledNews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawledNewsRepository extends JpaRepository<CrawledNews, Long> {
    boolean existsByOriginalUrl(String url);
}
