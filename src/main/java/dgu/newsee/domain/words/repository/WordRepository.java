package dgu.newsee.domain.words.repository;

import dgu.newsee.domain.words.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    List<Word> findByTermContainingOrDescriptionContaining(String termKeyword, String descKeyword);
}