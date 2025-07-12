package dgu.newsee.domain.transformednews.repository;

import dgu.newsee.domain.crawlednews.entity.NewsStatus;
import dgu.newsee.domain.transformednews.entity.NewsTransformed;
import dgu.newsee.domain.transformednews.entity.TransformLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NewsTransformedRepository extends JpaRepository<NewsTransformed, Long> {
    boolean existsByNewsId(Long newsId);

    Optional<NewsTransformed> findByNewsIdAndLevel(Long newsId, TransformLevel level);

    @Query("""
        SELECT t FROM NewsTransformed t
        WHERE t.level = :level
        AND t.status = :status
        AND (
             t.transformedContent LIKE %:keyword%
          OR t.news.title LIKE %:keyword%
        )
    """)
    List<NewsTransformed> searchByLevelAndKeywordAndStatus(
            @Param("level") TransformLevel level,
            @Param("keyword") String keyword,
            @Param("status") NewsStatus status
    );
}