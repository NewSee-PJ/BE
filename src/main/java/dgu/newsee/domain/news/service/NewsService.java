package dgu.newsee.domain.news.service;

import dgu.newsee.domain.news.dto.NewsCrawlRequestDTO;
import dgu.newsee.domain.news.entity.NewsOrigin;
import dgu.newsee.domain.news.entity.NewsStatus;
import dgu.newsee.domain.news.repository.NewsRepository;
import dgu.newsee.domain.news.util.NewsCrawlResult;
import dgu.newsee.domain.news.util.NewsCrawler;
import dgu.newsee.domain.user.entity.User;
import dgu.newsee.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsCrawler crawler;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    @Transactional
    public NewsOrigin crawlAndSave(NewsCrawlRequestDTO request, Long userId) {
        String url = request.getUrl();

        // 중복 저장 방지
        if (newsRepository.existsByOriginalUrl(url)) {
            throw new IllegalArgumentException("이미 저장된 뉴스입니다.");
        }

        try {
            // 뉴스 크롤링
            NewsCrawlResult result = crawler.crawl(url);

            // News 객체 저장
            NewsOrigin newsOrigin = NewsOrigin.builder()
                    .title(result.getTitle())
                    .content(result.getContent())
                    .category(result.getCategory())
                    .source(result.getSource())
                    .time(result.getTime())
                    .originalUrl(url)
                    .status(NewsStatus.USER_INPUT)
                    .build();
            newsRepository.save(newsOrigin);

            // 사용자 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

                        return newsOrigin;

        } catch (Exception e) {
            throw new RuntimeException("크롤링 실패: " + e.getMessage());
        }
    }
}
