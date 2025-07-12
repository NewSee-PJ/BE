package dgu.newsee.domain.news.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SavedNewsDTO {
    private Long savedNewsId;
    private Long newsId;
    private String title;
    private String category;
    private String source;
    private String time;
    private String url;
    private String imageUrl;
    private String transformedContent;
}