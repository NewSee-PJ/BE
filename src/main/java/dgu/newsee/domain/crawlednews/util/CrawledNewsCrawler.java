package dgu.newsee.domain.crawlednews.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CrawledNewsCrawler {

    public ParsedNews crawl(String url, String category) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return NewsParserUtil.parse(doc, category, url);  // 카테고리는 호출하는 쪽에서 지정
    }
}
