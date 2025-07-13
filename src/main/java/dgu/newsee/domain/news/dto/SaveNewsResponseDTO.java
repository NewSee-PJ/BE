package dgu.newsee.domain.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SaveNewsResponseDTO {
    private Long userId;
    private Long savedNewsId;
}
