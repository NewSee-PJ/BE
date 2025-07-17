package dgu.newsee.domain.transformednews.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dgu.newsee.domain.crawlednews.entity.NewsOrigin;
import dgu.newsee.domain.crawlednews.entity.NewsStatus;
import dgu.newsee.domain.crawlednews.repository.NewsRepository;
import dgu.newsee.global.exception.AiServerException;
import dgu.newsee.global.payload.ApiResponse;
import dgu.newsee.domain.transformednews.dto.TransformRequestDTO;
import dgu.newsee.domain.transformednews.dto.TransformedNewsResponseDTO;
import dgu.newsee.domain.transformednews.entity.NewsTransformed;
import dgu.newsee.domain.transformednews.entity.TransformLevel;
import dgu.newsee.domain.transformednews.repository.NewsTransformedRepository;
import dgu.newsee.domain.words.entity.Word;
import dgu.newsee.domain.words.repository.WordRepository;
import dgu.newsee.global.payload.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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

    private final String transformPath = "/api/news/transfer";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    @SneakyThrows
    public void requestTransformAndSaveAllLevels(Long newsId, NewsStatus status) {
        for (String level : List.of("상", "중", "하")) {
            requestTransformAndSave(newsId, level, status);
            Thread.sleep(9000);
        }
    }

    @Transactional
    public void requestTransformAndSave(Long newsId, String level, NewsStatus status) {
        NewsOrigin news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("뉴스 없음"));

        TransformRequestDTO request = new TransformRequestDTO(
                news.getTitle(),
                news.getContent(),
                level
        );

        // ai 서버 url + transformPath
        String requestUrl = aiServerUrl + transformPath;

        // 요청 로그 출력
        try {
            System.out.println("\n==== [AI 서버 요청 전송] ====");
            System.out.println("요청 URL: " + requestUrl);
            System.out.println("요청 JSON: " + objectMapper.writeValueAsString(request));
        } catch (Exception e) {
            System.out.println("요청 JSON 직렬화 실패: " + e.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TransformRequestDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ApiResponse<TransformedNewsResponseDTO>> response = null;

        try {
            response = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );
        } catch (Exception e) {
            System.out.println("AI 서버 호출 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
            throw new AiServerException(ResponseCode.AI_SERVER_DOWN);
        }

        // 응답 로그 출력
        try {
            System.out.println("==== [AI 서버 응답 수신] ====");
            if (response == null) {
                System.out.println("응답 객체가 null입니다.");
                throw new RuntimeException("응답이 null");
            }

            System.out.println("응답 상태 코드: " + response.getStatusCode());

            ApiResponse<TransformedNewsResponseDTO> apiResponse = response.getBody();

            if (apiResponse == null) {
                System.out.println("응답 바디가 null입니다.");
                throw new RuntimeException("response.getBody()가 null");
            }

            //System.out.println("응답 바디: " + objectMapper.writeValueAsString(apiResponse));

            if (apiResponse.getResult() == null) {
                System.out.println("result 필드가 null입니다.");
                throw new RuntimeException("AI 응답의 result가 null");
            }

            // 정상 응답 처리
            TransformedNewsResponseDTO result = apiResponse.getResult();

            NewsTransformed transformed = NewsTransformed.builder()
                    .news(news)
                    .level(TransformLevel.fromKorean(result.getLevel()))
                    .transformedContent(result.getTransformedContent())
                    .summarized(result.getSummarized())
                    .status(status)
                    .build();
            transformedRepository.save(transformed);
            System.out.println("변환된 뉴스 저장 완료");

            for (var wordDTO : result.getDifficultWords()) {
                if (!wordRepository.existsByTerm(wordDTO.getTerm())) {
                    Word word = Word.builder()
                            .term(wordDTO.getTerm())
                            .description(wordDTO.getDescription())
                            .category(news.getCategory())
                            .news(news)
                            .build();
                    wordRepository.save(word);
                }
            }

        } catch (Exception e) {
            System.out.println("응답 처리 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("응답 처리 실패");
        }
    }
}
