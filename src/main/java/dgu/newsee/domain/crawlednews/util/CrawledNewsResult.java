package dgu.newsee.domain.crawlednews.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CrawledNewsResult {
    private String title;
    private String content;
    private String category;
    private String source;
    private LocalDateTime time;
    private String url;
}
