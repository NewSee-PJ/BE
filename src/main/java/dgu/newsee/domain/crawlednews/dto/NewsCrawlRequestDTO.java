package dgu.newsee.domain.crawlednews.dto;

import lombok.Getter;

@Getter
public class NewsCrawlRequestDTO {
    private String url;
    private Long userId;
}
