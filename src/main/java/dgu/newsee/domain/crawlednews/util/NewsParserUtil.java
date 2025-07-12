package dgu.newsee.domain.crawlednews.util;

import org.jsoup.nodes.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NewsParserUtil {

    public static ParsedNews parse(Document doc, String categoryFromCaller, String url) {
        // 제목
        String title = doc.select("meta[property=og:title]").attr("content");

        // 본문
        String content = doc.select("#dic_area").text();

        // 출처
        String source = doc.select("meta[property=og:article:author]").attr("content");
        if (source.isBlank()) {
            source = doc.select("meta[property=og:site_name]").attr("content");
        }

        // 시간
        String rawTime = doc.select("meta[property=og:article:published_time]").attr("content");
        LocalDateTime time;
        try {
            time = LocalDateTime.parse(rawTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (Exception e) {
            time = LocalDateTime.now();
        }

        // 카테고리 유추
        String category = categoryFromCaller;
        if (category == null || category.isBlank()) {
            category = extractCategoryFromUrl(url);
        }

        return new ParsedNews(title, content, category, source, time, url);
    }

    private static String extractCategoryFromUrl(String url) {
        try {
            int sidIndex = url.indexOf("sid=");
            if (sidIndex != -1) {
                String sid = url.substring(sidIndex + 4, sidIndex + 7);
                return switch (sid) {
                    case "100" -> "정치";
                    case "101" -> "경제";
                    case "102" -> "사회";
                    case "103" -> "생활/문화";
                    case "104" -> "세계";
                    case "105" -> "IT/과학";
                    default -> "기타";
                };
            }
        } catch (Exception e) {
            // 무시하고 "기타"로 처리
        }
        return "기타";
    }
}
