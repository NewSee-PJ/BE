package dgu.newsee.domain.crawlednews.controller;

import dgu.newsee.domain.crawlednews.dto.NewsCrawlRequestDTO;
import dgu.newsee.domain.crawlednews.dto.NewsCrawlResponseDTO;
import dgu.newsee.domain.crawlednews.entity.NewsOrigin;
import dgu.newsee.domain.crawlednews.service.NewsService;
import dgu.newsee.global.payload.ApiResponse;
import dgu.newsee.global.payload.ResponseCode;
import dgu.newsee.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news/url")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    public ApiResponse<?> crawlNews(
            @RequestBody NewsCrawlRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 로그인 여부 확인
        if (userDetails == null) {
            return ApiResponse.failure(ResponseCode.USER_UNAUTHORIZED.getReason());
        }

        try {
            Long userId = userDetails.getUserId();
            NewsOrigin newsOrigin = newsService.crawlAndSave(request, userId);

            return ApiResponse.success(
                    new NewsCrawlResponseDTO(newsOrigin),
                    ResponseCode.COMMON_SUCCESS
            );
        } catch (IllegalArgumentException e) {
            return ApiResponse.failure(ResponseCode.NEWS_NOT_FOUND.getReason());
        } catch (Exception e) {
            return ApiResponse.failure(ResponseCode.COMMON_FAIL.getReason());
        }
    }
}
