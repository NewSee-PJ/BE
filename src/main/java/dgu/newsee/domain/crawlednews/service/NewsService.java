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
import dgu.newsee.global.security.CustomUserDetails;
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
    public NewsOrigin crawlAndSave(NewsCrawlRequestDTO request, CustomUserDetails userDetails) {
        String url = request.getUrl();

        // 사용자 조회
        //User user = userRepository.findById(userId)
                //.orElseThrow(() -> new UserException(ResponseCode.USER_UNAUTHORIZED));


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

            // 사용자 정보 기반 transform 처리
            if (userDetails == null) {
                // 비로그인 사용자
                transformedService.requestTransformAndSave(newsOrigin.getId(), "중", NewsStatus.USER_INPUT);
            } else {
                // 로그인: userId로 User 직접 조회해서 Level 추출
                User user = userRepository.findById(userDetails.getUserId())
                        .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));

                String userLevel = user.getLevel().getValue(); // 예: "상", "중", "하"
                transformedService.requestTransformAndSave(newsOrigin.getId(), userLevel, NewsStatus.USER_INPUT);
            }

            return newsOrigin;

        } catch (Exception e) {
            throw new NewsException(ResponseCode.NEWS_CRAWL_FAIL);
        }
    }
}
