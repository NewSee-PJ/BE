package dgu.newsee.domain.transformednews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TransformedNewsResponseDTO {
    private Long newsId;
    private String level;
    private String title;
    private String transformedContent;
    private String summarized;
    private List<DifficultWordDTO> difficultWords;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DifficultWordDTO {
        private String term;
        private String description;
    }
}
