package dgu.newsee.domain.news.repository;

import dgu.newsee.domain.crawlednews.entity.NewsOrigin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsQueryRepository extends JpaRepository<NewsOrigin, Long> {

    // 자동 크롤링만 가져오기
    List<NewsOrigin> findByStatus(dgu.newsee.domain.crawlednews.entity.NewsStatus status);

}