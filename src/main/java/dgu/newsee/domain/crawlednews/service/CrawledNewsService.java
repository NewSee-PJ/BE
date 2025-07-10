package dgu.newsee.domain.crawlednews.service;

import dgu.newsee.domain.crawlednews.entity.CrawledNews;
import dgu.newsee.domain.crawlednews.repository.CrawledNewsRepository;
import dgu.newsee.domain.crawlednews.util.CrawledNewsCrawler;
import dgu.newsee.domain.crawlednews.util.CrawledNewsResult;
import dgu.newsee.domain.transformednews.service.CrawledTransformedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CrawledNewsService {

    private final CrawledNewsCrawler crawler;
    private final CrawledNewsRepository repository;
    private final CrawledTransformedService crawledTransformedService;

    @Transactional
    public void crawlAndSave(String url, String category) {
        String normalizedUrl = url.replace("/comment", "").split("\\?")[0];

        if (repository.existsByOriginalUrl(normalizedUrl)) {
            System.out.println("중복된 뉴스 URL → 저장하지 않음: " + normalizedUrl);
            return;
        }

        try {
            CrawledNewsResult result = crawler.crawl(normalizedUrl, category);
            CrawledNews news = CrawledNews.builder()
                    .title(result.getTitle())
                    .content(result.getContent())
                    .category(result.getCategory())
                    .source(result.getSource())
                    .time(result.getTime())
                    .originalUrl(normalizedUrl)
                    .build();
            repository.save(news);
            System.out.println("크롤링 및 저장 완료: " + normalizedUrl);
            crawledTransformedService.requestTransformAndSave(news.getId(), null); // 기본 level은 "중"

        } catch (Exception e) {
            System.err.println("크롤링 실패: " + normalizedUrl + " → " + e.getMessage());
        }
    }
}
