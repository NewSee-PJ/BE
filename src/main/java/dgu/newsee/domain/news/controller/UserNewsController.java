package dgu.newsee.domain.news.controller;

import dgu.newsee.domain.news.dto.NewsDetailDTO;
import dgu.newsee.domain.news.dto.NewsDTO;
import dgu.newsee.domain.news.service.NewsService;
import dgu.newsee.domain.news.util.SecurityUtil;
import dgu.newsee.domain.user.repository.UserRepository;
import dgu.newsee.global.exception.NewsException;
import dgu.newsee.global.payload.ApiResponse;
import dgu.newsee.global.payload.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class UserNewsController {

    private final NewsService newsService;
    private final UserRepository userRepository;

    @GetMapping
    @Operation(summary = "전체 뉴스 조회 (비회원은 중 레벨)")
    public ApiResponse<List<NewsDTO>> getAllNews(Authentication authentication) {
        Long userId = SecurityUtil.extractUserId(authentication);
        return newsService.getAllNews(userId, null);
    }

    @GetMapping("/{newsId}")
    @Operation(summary = "뉴스 상세 조회 (비회원은 중 레벨)")
    public ApiResponse<NewsDetailDTO> getNewsDetail(@PathVariable Long newsId, Authentication authentication) {
        Long userId = SecurityUtil.extractUserId(authentication);
        return newsService.getNewsDetail(newsId, userId, null);
    }

    @GetMapping("/search")
    @Operation(summary = "뉴스 검색 (비회원은 중 레벨)")
    public ApiResponse<List<NewsDTO>> searchNews(@RequestParam String keyword, Authentication authentication) {
        Long userId = SecurityUtil.extractUserId(authentication);
        return newsService.searchNews(keyword, userId, null);
    }

    @PostMapping("/{newsId}")
    @Operation(summary = "뉴스 북마크 저장 (회원만 가능)")
    public ApiResponse<?> saveNews(@PathVariable Long newsId, Authentication authentication) {
        Long userId = SecurityUtil.extractUserId(authentication);

        if (userId == null) {
            throw new NewsException(ResponseCode.USER_UNAUTHORIZED);
        }

        return newsService.saveNews(userId, newsId, null);
    }

    @DeleteMapping("/{savedNewsId}")
    @Operation(summary = "뉴스 북마크 삭제 (회원만 가능)")
    public ApiResponse<?> deleteNewsBookmark(
            @PathVariable Long savedNewsId,
            Authentication authentication
    ) {
        Long userId = SecurityUtil.extractUserId(authentication);

        if (userId == null) {
            throw new NewsException(ResponseCode.USER_UNAUTHORIZED);
        }

        return newsService.deleteNewsBookmark(userId, savedNewsId);
    }

}