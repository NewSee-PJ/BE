package dgu.newsee.domain.news.controller;

import dgu.newsee.domain.news.dto.NewsCrawlRequestDTO;
import dgu.newsee.domain.news.dto.NewsCrawlResponseDTO;
import dgu.newsee.domain.news.entity.News;
import dgu.newsee.domain.news.service.NewsService;
import dgu.newsee.global.payload.ApiResponse;
import dgu.newsee.global.payload.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/url/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    public ApiResponse<?> crawlNews(@RequestBody NewsCrawlRequestDTO request) {
        try {
            News news = newsService.crawlAndSave(request);
            return ApiResponse.success(
                    new NewsCrawlResponseDTO(news), ResponseCode.COMMON_SUCCESS
            );
        } catch (IllegalArgumentException e) {
            return ApiResponse.failure(ResponseCode.NEWS_NOT_FOUND.getReason());
        } catch (Exception e) {
            return ApiResponse.failure(ResponseCode.COMMON_FAIL.getReason());

        }
    }
}

