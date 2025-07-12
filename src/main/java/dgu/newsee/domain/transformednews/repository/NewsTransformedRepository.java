package dgu.newsee.domain.transformednews.repository;

import dgu.newsee.domain.transformednews.entity.NewsTransformed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsTransformedRepository extends JpaRepository<NewsTransformed, Long> {
    boolean existsByNewsId(Long newsId);
}
