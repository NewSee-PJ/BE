package dgu.newsee.domain.news.util;

import dgu.newsee.domain.news.util.NewsCrawlResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class NewsCrawler {

    public NewsCrawlResult crawl(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        // 제목
        String title = doc.select("meta[property=og:title]").attr("content");

        // 본문
        String content = doc.select("#dic_area").text();

        // 카테고리 추출 (네이버는 명확하게 드러나진 않음 → default로 지정)
        String category = "기타";

        // 출처 (언론사 이름)
        String source = doc.select("meta[property=og:article:author]").attr("content");
        if (source.isBlank()) {
            source = doc.select("meta[property=og:site_name]").attr("content"); // fallback
        }

        // 작성 시간 (문자열 → LocalDateTime 파싱)
        String rawTime = doc.select("meta[property=og:article:published_time]").attr("content");
        LocalDateTime time;
        try {
            time = LocalDateTime.parse(rawTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (Exception e) {
            time = LocalDateTime.now(); // fallback
        }

        return new NewsCrawlResult(title, content, category, source, time);
    }
}
