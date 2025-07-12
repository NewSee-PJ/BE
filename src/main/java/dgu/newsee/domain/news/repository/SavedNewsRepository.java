package dgu.newsee.domain.news.repository;

import dgu.newsee.domain.news.entity.SavedNews;
import org.springframework.data.jpa.repository.JpaRepository;
import dgu.newsee.domain.transformednews.entity.TransformLevel;

import java.util.List;
import java.util.Optional;

public interface SavedNewsRepository extends JpaRepository<SavedNews, Long> {
    List<SavedNews> findByUserId(Long userId);

    boolean existsByUserIdAndNewsOriginIdAndSavedLevel(Long userId, Long newsOriginId, TransformLevel savedLevel);
}
