package dgu.newsee.domain.crawlednews.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NewsParserUtil {

    public static ParsedNews parse(Document doc, String categoryFromCaller, String url) {
        // 제목
        String title = doc.select("meta[property=og:title]").attr("content");

        // 본문
        String content;

        Elements paragraphs = doc.select("#dic_area > p");
        if (!paragraphs.isEmpty()) {
            // <p> 태그가 있는 경우 문단 단위 추출
            List<String> lines = new ArrayList<>();
            for (Element p : paragraphs) {
                String text = p.text().trim();
                if (!text.isEmpty()) lines.add(text);
            }
            content = String.join("\n", lines);
        } else {
            // <p> 태그가 없는 경우 전체 텍스트 추출
            Element dicArea = doc.selectFirst("#dic_area");
            content = (dicArea != null) ? dicArea.text().trim() : "";
        }

        System.out.println("본문 내용: " + content);


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

        // 1. 시스템 크롤링이면 외부에서 categoryFromCaller가 전달됨
        if (categoryFromCaller != null && !categoryFromCaller.isBlank()) {
            category = categoryFromCaller;
        }

        // 2. 사용자 입력 등인 경우 HTML 태그 기반 시도
        if (category == null || category.isBlank()) {
            Element selected = doc.selectFirst("a.Nitem_link_menu[aria-selected=true], span.Nitem_link_menu");
            if (selected != null) {
                category = selected.text();
            }
        }

        // 3. 그래도 없으면 URL 내 sid= 파싱 시도
        if ((category == null || category.isBlank()) && url.contains("sid=")) {
            category = extractCategoryFromUrl(url);
        }

        // 4. 여전히 못 찾으면 fallback
        if (category == null || category.isBlank()) {
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
