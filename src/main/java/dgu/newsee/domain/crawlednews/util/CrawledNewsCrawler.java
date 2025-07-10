package dgu.newsee.domain.crawlednews.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CrawledNewsCrawler {

    public CrawledNewsResult crawl(String url, String category) throws IOException {
        Document doc = Jsoup.connect(url).get();

        String title = doc.select("meta[property=og:title]").attr("content");
        String content = doc.select("#dic_area").text();
        String source = doc.select("meta[property=og:article:author]").attr("content");
        if (source.isBlank()) {
            source = doc.select("meta[property=og:site_name]").attr("content");
        }

        String rawTime = doc.select("meta[property=og:article:published_time]").attr("content");
        LocalDateTime time;
        try {
            time = LocalDateTime.parse(rawTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (Exception e) {
            time = LocalDateTime.now();
        }

        return new CrawledNewsResult(title, content, category, source, time, url);
    }
}
