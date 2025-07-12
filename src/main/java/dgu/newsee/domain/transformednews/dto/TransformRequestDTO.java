package dgu.newsee.domain.transformednews.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class TransformRequestDTO {
    private String title;
    private String originalContent;
    private String level;
}
