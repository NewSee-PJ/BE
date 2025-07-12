package dgu.newsee.domain.transformednews.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dgu.newsee.domain.crawlednews.entity.NewsOrigin;
import dgu.newsee.domain.crawlednews.entity.NewsStatus;
import dgu.newsee.domain.crawlednews.repository.NewsRepository;
import dgu.newsee.domain.transformednews.dto.TransformRequestDTO;
import dgu.newsee.domain.transformednews.dto.TransformedNewsResponseDTO;
import dgu.newsee.domain.transformednews.entity.NewsTransformed;
import dgu.newsee.domain.transformednews.entity.TransformLevel;
import dgu.newsee.domain.transformednews.repository.NewsTransformedRepository;
import dgu.newsee.domain.words.entity.Word;
import dgu.newsee.domain.words.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransformedNewsService {

    private final NewsRepository newsRepository;
    private final NewsTransformedRepository transformedRepository;
    private final WordRepository wordRepository;
    private final RestTemplate restTemplate = new RestTemplate();

        @Value("${external.ai.url}")
        private String aiServerUrl;

        @Transactional
        public void requestTransformAndSaveAllLevels(Long newsId, NewsStatus status) {
            for (String level : List.of("상", "중", "하")) {
                requestTransformAndSave(newsId, level, status);
            }
        }

        @Transactional
        public void requestTransformAndSave(Long newsId, String level, NewsStatus status) {
            NewsOrigin news = newsRepository.findById(newsId)
                    .orElseThrow(() -> new RuntimeException("뉴스 없음"));

            TransformRequestDTO request = new TransformRequestDTO(
                    newsId,
                    news.getTitle(),
                    news.getContent(),
                    level
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<TransformRequestDTO> entity = new HttpEntity<>(request, headers);

            ResponseEntity<TransformedNewsResponseDTO> response = restTemplate.exchange(
                    aiServerUrl, HttpMethod.POST, entity, TransformedNewsResponseDTO.class
            );

            TransformedNewsResponseDTO result = response.getBody();
            if (result == null) throw new RuntimeException("AI 응답 없음");

            NewsTransformed transformed = NewsTransformed.builder()
                    .news(news)
                    .level(TransformLevel.valueOf(result.getLevel()))
                    .transformedContent(result.getTransformedContent())
                    .summarized(result.getSummarized())
                    .status(status)
                    .build();
            transformedRepository.save(transformed);

            for (var wordDTO : result.getDifficultWords()) {
                if (!wordRepository.existsByTerm(wordDTO.getTerm())) {
                    Word word = Word.builder()
                            .term(wordDTO.getTerm())
                            .description(wordDTO.getDescription())
                            .category("뉴스 변환")
                            .build();
                    wordRepository.save(word);
                }
            }
        }
    }
