package dgu.newsee.domain.crawlednews.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NewsCrawler {

    public ParsedNews crawl(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return NewsParserUtil.parse(doc, null, url);
    }
}
