package dgu.newsee.domain.crawlednews.util;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SectionUrlCollector {

    public List<String> collect(String sectionUrl) {
        List<String> articleUrls = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(sectionUrl).get();
            Elements links = doc.select("a[href^=https://n.news.naver.com/mnews/article/]");

            for (var link : links) {
                String href = link.attr("abs:href");
                if (!articleUrls.contains(href)) {
                    articleUrls.add(href);
                }
            }
        } catch (Exception e) {
            log.error("뉴스 링크 수집 실패 - {}", sectionUrl, e);
        }

        return articleUrls;
    }
}
