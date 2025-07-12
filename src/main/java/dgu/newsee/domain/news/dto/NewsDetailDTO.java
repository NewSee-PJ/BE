package dgu.newsee.domain.news.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class NewsDetailDTO {
    private Long newsId;
    private String title;
    private String category;
    private String source;
    private String time;
    private String url;
    private String imageUrl;
    private boolean isBookmarked;

    @JsonProperty("isBookmarked")
    public boolean getIsBookmarked() {
        return isBookmarked;
    }

    private String transformedContent;
    private String summary;
    private List<KeywordDto> keywords;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class KeywordDto {
        private Long wordId;
        private String term;
        private String description;
        private String source;
    }
}