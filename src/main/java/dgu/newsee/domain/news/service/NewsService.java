package dgu.newsee.domain.news.service;


import dgu.newsee.domain.news.dto.NewsCrawlRequestDTO;
import dgu.newsee.domain.news.util.NewsCrawlResult;
import dgu.newsee.domain.news.entity.News;
import dgu.newsee.domain.news.repository.NewsRepository;
import dgu.newsee.domain.news.util.NewsCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsCrawler crawler;
    private final NewsRepository newsRepository;

    @Transactional
    public News crawlAndSave(NewsCrawlRequestDTO request) {
        String url = request.getUrl();

        if (newsRepository.existsByOriginalUrl(url)) {
            throw new IllegalArgumentException("이미 저장된 뉴스입니다.");
        }

        try {
            NewsCrawlResult result = crawler.crawl(url);

            News news = News.builder()
                    .title(result.getTitle())
                    .content(result.getContent())
                    .category(result.getCategory())
                    .source(result.getSource())
                    .time(result.getTime())
                    .originalUrl(url)
                    .build();

            return newsRepository.save(news);
        } catch (Exception e) {
            throw new RuntimeException("크롤링 실패: " + e.getMessage());
        }
    }
}

