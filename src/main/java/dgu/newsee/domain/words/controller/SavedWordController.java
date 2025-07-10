package dgu.newsee.domain.words.controller;

import dgu.newsee.domain.words.service.SavedWordService;
import dgu.newsee.domain.words.dto.SavedWordDTO.WordResponse.SavedWordListResponse;
import dgu.newsee.domain.words.dto.SavedWordDTO.WordResponse.SaveResult;
import dgu.newsee.domain.words.dto.SavedWordDTO.WordResponse.DeleteResult;
import dgu.newsee.global.exception.UserException;
import dgu.newsee.global.payload.ApiResponse;
import dgu.newsee.global.payload.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
@Tag(name = "Word 컨트롤러", description = "단어장 관련 API")
public class SavedWordController {

    private final SavedWordService wordService;

    @PostMapping("/{wordId}")
    @Operation(summary = "단어장에 단어 추가")
    public ApiResponse<SaveResult> saveWord(Authentication authentication, @PathVariable Long wordId) {
        if (authentication == null) {
            throw new UserException(ResponseCode.USER_UNAUTHORIZED);
        }
        String userId = authentication.getName();
        return ApiResponse.success(
                wordService.saveWord(userId, wordId)
        );
    }

    @GetMapping
    @Operation(summary = "저장한 단어 전체 조회")
    public ApiResponse<SavedWordListResponse> getAllWords(Authentication authentication) {
        if (authentication == null) {
            throw new UserException(ResponseCode.USER_UNAUTHORIZED);
        }
        String userId = authentication.getName();
        return ApiResponse.success(
                wordService.getAllWords(userId)
        );
    }

    @GetMapping("/search")
    @Operation(summary = "단어장 내 단어 검색")
    public ApiResponse<SavedWordListResponse> searchWord(Authentication authentication, @RequestParam String keyword) {
        if (authentication == null) {
            throw new UserException(ResponseCode.USER_UNAUTHORIZED);
        }
        String userId = authentication.getName();
        return ApiResponse.success(
                wordService.searchWords(userId, keyword)
        );
    }

    @DeleteMapping("/{savedWordId}")
    @Operation(summary = "단어 삭제 (savedWordId 기준)")
    public ApiResponse<DeleteResult> deleteWord(Authentication authentication, @PathVariable Long savedWordId) {
        if (authentication == null) {
            throw new UserException(ResponseCode.USER_UNAUTHORIZED);
        }
        String userId = authentication.getName();
        return ApiResponse.success(
                wordService.deleteWord(userId, savedWordId)
        );
    }
}