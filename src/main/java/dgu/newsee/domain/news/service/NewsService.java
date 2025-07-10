package dgu.newsee.domain.news.service;

import dgu.newsee.domain.news.dto.NewsCrawlRequestDTO;
import dgu.newsee.domain.news.entity.News;
import dgu.newsee.domain.news.repository.NewsRepository;
import dgu.newsee.domain.news.util.NewsCrawlResult;
import dgu.newsee.domain.news.util.NewsCrawler;
import dgu.newsee.domain.user.entity.User;
import dgu.newsee.domain.user.repository.UserRepository;
import dgu.newsee.domain.news.entity.SavedNews;
import dgu.newsee.domain.news.repository.SavedNewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsCrawler crawler;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final SavedNewsRepository savedNewsRepository;

    @Transactional
    public News crawlAndSave(NewsCrawlRequestDTO request, Long userId) {
        String url = request.getUrl();

        // 중복 저장 방지
        if (newsRepository.existsByOriginalUrl(url)) {
            throw new IllegalArgumentException("이미 저장된 뉴스입니다.");
        }

        try {
            // 뉴스 크롤링
            NewsCrawlResult result = crawler.crawl(url);

            // News 객체 저장
            News news = News.builder()
                    .title(result.getTitle())
                    .content(result.getContent())
                    .category(result.getCategory())
                    .source(result.getSource())
                    .time(result.getTime())
                    .originalUrl(url)
                    .build();
            newsRepository.save(news);

            // 사용자 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            // 사용자와 뉴스 연결 (SavedNews 테이블에 저장)
            SavedNews savedNews = SavedNews.builder()
                    .user(user)
                    .news(news)
                    .build();
            savedNewsRepository.save(savedNews);

            return news;

        } catch (Exception e) {
            throw new RuntimeException("크롤링 실패: " + e.getMessage());
        }
    }
}
