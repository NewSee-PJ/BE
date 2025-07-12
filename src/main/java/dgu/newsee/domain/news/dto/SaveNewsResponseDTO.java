package dgu.newsee.domain.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveNewsResponseDTO {
    private Long userId;
    private Long newsId;
}
