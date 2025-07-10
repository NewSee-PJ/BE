package dgu.newsee.domain.transformednews.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dgu.newsee.domain.crawlednews.entity.CrawledNews;
import dgu.newsee.domain.crawlednews.repository.CrawledNewsRepository;
import dgu.newsee.domain.transformednews.dto.TransformRequestDTO;
import dgu.newsee.domain.transformednews.dto.TransformedNewsResponseDTO;
import dgu.newsee.domain.transformednews.entity.CrawledNewsTransformed;
import dgu.newsee.domain.transformednews.repository.CrawledNewsTransformedRepository;
import dgu.newsee.domain.words.entity.Word;
import dgu.newsee.domain.words.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CrawledTransformedService {

    private final CrawledNewsRepository crawledNewsRepository;
    private final CrawledNewsTransformedRepository crawledTransformedRepository;
    private final WordRepository wordRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${external.ai.url}")
    private String aiServerUrl;

    @Transactional
    public void requestTransformAndSave(Long crawledNewsId, String level) {
        CrawledNews news = crawledNewsRepository.findById(crawledNewsId)
                .orElseThrow(() -> new RuntimeException("크롤링 뉴스 없음"));

        TransformRequestDTO request = new TransformRequestDTO(
                news.getId(),
                news.getTitle(),
                news.getContent(),
                level == null ? "중" : level
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TransformRequestDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<TransformedNewsResponseDTO> response = restTemplate.exchange(
                aiServerUrl, HttpMethod.POST, entity, TransformedNewsResponseDTO.class
        );

        TransformedNewsResponseDTO result = response.getBody();
        if (result == null) throw new RuntimeException("AI 응답 없음");

        CrawledNewsTransformed transformed = CrawledNewsTransformed.builder()
                .crawledNews(news)
                .level(result.getLevel())
                .transformedContent(result.getTransformedContent())
                .summarized(result.getSummarized())
                .build();
        crawledTransformedRepository.save(transformed);

        for (var wordDTO : result.getDifficultWords()) {
            if (!wordRepository.existsByTerm(wordDTO.getTerm())) {
                Word word = Word.builder()
                        .term(wordDTO.getTerm())
                        .description(wordDTO.getDescription())
                        .category("크롤링 뉴스 변환")
                        .build();
                wordRepository.save(word);
            }
        }
    }
}
