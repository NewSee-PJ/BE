package dgu.newsee.domain.crawlednews.service;

import dgu.newsee.domain.crawlednews.util.SectionUrlCollector;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CrawledNewsScheduler {

    private final CrawledNewsService service;
    private final SectionUrlCollector collector;

    private final Map<String, String> categoryMap = Map.of(
            "정치", "https://news.naver.com/section/100",
            "경제", "https://news.naver.com/section/101",
            "사회", "https://news.naver.com/section/102",
            "생활/문화", "https://news.naver.com/section/103",
            "IT/과학", "https://news.naver.com/section/105",
            "세계", "https://news.naver.com/section/104"
    );

    @Scheduled(cron = "0 0 */12 * * *") // 12시간마다
    public void crawlNewsPeriodically() {
        for (String category : categoryMap.keySet()) {
            String sectionUrl = categoryMap.get(category);
            List<String> urls = collector.collect(sectionUrl);
            urls.stream().limit(10).forEach(url -> service.crawlAndSave(url, category));
        }
    }
}

