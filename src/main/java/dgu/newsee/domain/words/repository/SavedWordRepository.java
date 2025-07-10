package dgu.newsee.domain.words.repository;

import dgu.newsee.domain.words.entity.SavedWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedWordRepository extends JpaRepository<SavedWord, Long> {
    List<SavedWord> findByUserId(Long userId);
    Optional<SavedWord> findByUserIdAndWordId(Long userId, Long wordId);
}