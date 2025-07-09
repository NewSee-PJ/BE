package dgu.newsee.domain.news.dto;

import dgu.newsee.domain.news.entity.News;
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

    public NewsCrawlResponseDTO(News news) {
        this.title = news.getTitle();
        this.content = news.getContent();
        this.category = news.getCategory();
        this.source = news.getSource();
        this.time = news.getTime();
        this.newsId = news.getId();
    }
}

