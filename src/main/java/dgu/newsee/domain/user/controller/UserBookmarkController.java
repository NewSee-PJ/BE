package dgu.newsee.domain.user.controller;

import dgu.newsee.domain.news.dto.SavedNewsDTO;
import dgu.newsee.domain.news.service.NewsService;
import dgu.newsee.global.exception.NewsException;
import dgu.newsee.global.payload.ApiResponse;
import dgu.newsee.global.payload.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dgu.newsee.domain.news.util.SecurityUtil;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/saved-news")
public class UserBookmarkController {

    private final NewsService newsService;

    @GetMapping
    @Operation(summary = "북마크한 뉴스 목록 조회 (회원만 가능)")
    public ApiResponse<List<SavedNewsDTO>> getSavedNews(Authentication authentication) {
        Long userId = SecurityUtil.extractUserId(authentication);
        if (userId == null) {
            throw new NewsException(ResponseCode.USER_UNAUTHORIZED);
        }
        return newsService.getSavedNews(userId, null);
    }
}
