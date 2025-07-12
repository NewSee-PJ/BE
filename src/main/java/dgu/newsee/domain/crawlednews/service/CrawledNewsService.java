package dgu.newsee.domain.crawlednews.service;

import dgu.newsee.domain.crawlednews.entity.NewsOrigin;
import dgu.newsee.domain.crawlednews.entity.NewsStatus;
import dgu.newsee.domain.crawlednews.repository.NewsRepository;
import dgu.newsee.domain.crawlednews.util.CrawledNewsCrawler;
import dgu.newsee.domain.crawlednews.util.ParsedNews;
import dgu.newsee.domain.transformednews.service.TransformedNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawledNewsService {

    private final CrawledNewsCrawler crawler;
    private final NewsRepository newsRepository;
    private final TransformedNewsService transformedNewsService;


    @Transactional
    public void crawlAndSave(String url, String category) {
        String normalizedUrl = url.replace("/comment", "").split("\\?")[0];

        if (newsRepository.existsByOriginalUrl(normalizedUrl)) {
            System.out.println("중복된 뉴스 URL → 저장하지 않음: " + normalizedUrl);
            return;
        }

        try {
            ParsedNews result = crawler.crawl(normalizedUrl, category);

            NewsOrigin news = NewsOrigin.builder()
                    .title(result.getTitle())
                    .content(result.getContent())
                    .category(result.getCategory())
                    .source(result.getSource())
                    .time(result.getTime())
                    .originalUrl(normalizedUrl)
                    .status(NewsStatus.AUTO_CRAWLED)
                    .build();
            newsRepository.save(news);
            System.out.println("크롤링 및 저장 완료: " + normalizedUrl);

            transformedNewsService.requestTransformAndSaveAllLevels(news.getId(), NewsStatus.AUTO_CRAWLED);

        } catch (Exception e) {
            System.err.println("크롤링 실패: " + normalizedUrl + " → " + e.getMessage());
        }
    }
}
