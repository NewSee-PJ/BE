package dgu.newsee.domain.transformednews.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransformRequestDTO {
    private Long newsId;
    private String title;
    private String originalContent;
    private String level; // default는 "중"
}
