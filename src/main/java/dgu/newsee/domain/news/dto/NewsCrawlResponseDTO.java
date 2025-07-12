package dgu.newsee.domain.news.dto;

import dgu.newsee.domain.news.entity.NewsOrigin;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NewsCrawlResponseDTO {
    private String title;
    private String content;
    private String category;
    private String source;
    private LocalDateTime time;
    private Long newsId;

    public NewsCrawlResponseDTO(NewsOrigin newsOrigin) {
        this.title = newsOrigin.getTitle();
        this.content = newsOrigin.getContent();
        this.category = newsOrigin.getCategory();
        this.source = newsOrigin.getSource();
        this.time = newsOrigin.getTime();
        this.newsId = newsOrigin.getId();
    }
}

