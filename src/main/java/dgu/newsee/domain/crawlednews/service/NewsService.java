package dgu.newsee.domain.crawlednews.service;

import dgu.newsee.domain.crawlednews.dto.NewsCrawlRequestDTO;
import dgu.newsee.domain.crawlednews.entity.NewsOrigin;
import dgu.newsee.domain.crawlednews.entity.NewsStatus;
import dgu.newsee.domain.crawlednews.repository.NewsRepository;
import dgu.newsee.domain.crawlednews.util.NewsCrawler;
import dgu.newsee.domain.crawlednews.util.ParsedNews;
import dgu.newsee.domain.transformednews.service.TransformedNewsService;
import dgu.newsee.domain.user.entity.User;
import dgu.newsee.domain.user.repository.UserRepository;
import dgu.newsee.global.exception.NewsException;
import dgu.newsee.global.exception.UserException;
import dgu.newsee.global.payload.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsCrawler crawler;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final TransformedNewsService transformedService;

    @Transactional
    public NewsOrigin crawlAndSave(NewsCrawlRequestDTO request, Long userId) {
        String url = request.getUrl();

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ResponseCode.USER_UNAUTHORIZED));


        // 1. 이미 저장된 뉴스면 바로 반환
        Optional<NewsOrigin> optionalNews = newsRepository.findByOriginalUrl(url);
        if (optionalNews.isPresent()) {
            return optionalNews.get(); // 안전하게 꺼내기
        }

        try {
            // 뉴스 크롤링
            ParsedNews result = crawler.crawl(url);

            // News 객체 저장
            NewsOrigin newsOrigin = NewsOrigin.builder()
                    .title(result.getTitle())
                    .content(result.getContent())
                    .imageUrl(result.getImageUrl())
                    .category(result.getCategory())
                    .source(result.getSource())
                    .time(result.getTime())
                    .originalUrl(url)
                    .status(NewsStatus.USER_INPUT)
                    .build();
            newsRepository.save(newsOrigin);

            transformedService.requestTransformAndSaveAllLevels(
                    newsOrigin.getId(),
                    NewsStatus.USER_INPUT
            );
            return newsOrigin;

        } catch (Exception e) {
            throw new NewsException(ResponseCode.NEWS_CRAWL_FAIL);
        }
    }
}
