package dgu.newsee.domain.news.controller;

import dgu.newsee.domain.news.dto.NewsDetailDTO;
import dgu.newsee.domain.news.dto.NewsDTO;
import dgu.newsee.domain.news.dto.SavedNewsDTO;
import dgu.newsee.domain.news.service.NewsService;
import dgu.newsee.domain.user.entity.Level;
import dgu.newsee.domain.user.entity.User;
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
        Long userId = extractUserId(authentication);
        return newsService.getAllNews(userId, null);
    }

    @GetMapping("/{newsId}")
    @Operation(summary = "뉴스 상세 조회 (비회원은 중 레벨)")
    public ApiResponse<NewsDetailDTO> getNewsDetail(@PathVariable Long newsId, Authentication authentication) {
        Long userId = extractUserId(authentication);
        return newsService.getNewsDetail(newsId, userId, null);
    }

    @GetMapping("/search")
    @Operation(summary = "뉴스 검색 (비회원은 중 레벨)")
    public ApiResponse<List<NewsDTO>> searchNews(@RequestParam String keyword, Authentication authentication) {
        Long userId = extractUserId(authentication);
        return newsService.searchNews(keyword, userId, null);
    }

    @GetMapping("/user/saved-news")
    @Operation(summary = "북마크한 뉴스 목록 조회 (회원만 가능)")
    public ApiResponse<List<SavedNewsDTO>> getSavedNews(Authentication authentication) {
        Long userId = extractUserId(authentication);
        if (userId == null) {
            throw new NewsException(ResponseCode.USER_UNAUTHORIZED);
        }
        return newsService.getSavedNews(userId, null);
    }

    @PostMapping("/{newsId}")
    @Operation(summary = "뉴스 북마크 저장 (회원만 가능)")
    public ApiResponse<?> saveNews(@PathVariable Long newsId, Authentication authentication) {
        Long userId = extractUserId(authentication);

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
        Long userId = extractUserId(authentication);

        if (userId == null) {
            throw new NewsException(ResponseCode.USER_UNAUTHORIZED);
        }

        return newsService.deleteNewsBookmark(userId, savedNewsId);
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        return Long.parseLong(authentication.getName());
    }
}