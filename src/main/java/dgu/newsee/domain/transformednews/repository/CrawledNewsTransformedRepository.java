package dgu.newsee.domain.transformednews.repository;

import dgu.newsee.domain.transformednews.entity.CrawledNewsTransformed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawledNewsTransformedRepository extends JpaRepository<CrawledNewsTransformed, Long> {
    boolean existsByCrawledNewsId(Long crawledNewsId);
}
