package dgu.newsee.domain.news.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NewsCrawlResult {
    private String title;
    private String content;
    private String category;
    private String source;
    private LocalDateTime time;
}

