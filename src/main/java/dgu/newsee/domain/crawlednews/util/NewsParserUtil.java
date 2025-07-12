package dgu.newsee.domain.crawlednews.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

        // 대표 이미지
        String imageUrl = doc.select("meta[property=og:image]").attr("content");

        if (imageUrl == null || imageUrl.isBlank()) {
            try {
                imageUrl = doc.select("img[src]").stream()
                        .map(e -> e.attr("src"))
                        .filter(src -> src.contains("imgnews.pstatic.net"))
                        .findFirst()
                        .orElse(null);
            } catch (Exception e) {
                // 무시
            }
        }
        System.out.println("대표 이미지 URL 최종: " + imageUrl);



        // 카테고리 유추
        String category = null;

        try {
            // 1. 네이버 뉴스일 경우 카테고리 직접 파싱 시도
            Element selected = doc.selectFirst("a.Nitem_link_menu[aria-selected=true]");
            if (selected != null) {
                category = selected.text();  // 예: 생활/문화
            }

            // 2. 그래도 null이면 백업으로 URL에서 유추 시도
            if (category == null || category.isBlank()) {
                category = extractCategoryFromUrl(url); // sid 기반
            }

            // 3. 여전히 못찾으면 fallback
            if (category == null || category.isBlank()) {
                category = "기타";
            }

        } catch (Exception e) {
            category = "기타";
        }


        return new ParsedNews(title, content, category, source, time, url, imageUrl);
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
