package dgu.newsee.domain.news.service;

import dgu.newsee.domain.crawlednews.entity.NewsOrigin;
import dgu.newsee.domain.crawlednews.entity.NewsStatus;
import dgu.newsee.domain.news.converter.NewsConverter;
import dgu.newsee.domain.news.dto.NewsDetailDTO;
import dgu.newsee.domain.news.dto.NewsDTO;
import dgu.newsee.domain.news.dto.SaveNewsResponseDTO;
import dgu.newsee.domain.news.dto.SavedNewsDTO;
import dgu.newsee.domain.news.repository.NewsQueryRepository;
import dgu.newsee.domain.transformednews.entity.NewsTransformed;
import dgu.newsee.domain.transformednews.entity.TransformLevel;
import dgu.newsee.domain.transformednews.repository.NewsTransformedRepository;
import dgu.newsee.domain.news.entity.SavedNews;
import dgu.newsee.domain.news.repository.SavedNewsRepository;
import dgu.newsee.domain.words.repository.WordRepository;
import dgu.newsee.domain.words.entity.Word;
import dgu.newsee.global.exception.NewsException;
import dgu.newsee.global.payload.ApiResponse;
import dgu.newsee.global.payload.ResponseCode;
import dgu.newsee.domain.user.entity.Level;
import dgu.newsee.domain.user.entity.User;
import dgu.newsee.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsQueryRepository newsQueryRepository;
    private final NewsTransformedRepository newsTransformedRepository;
    private final SavedNewsRepository savedNewsRepository;
    private final WordRepository wordRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<List<NewsDTO>> getAllNews(Long userId, String levelKor) {
        TransformLevel level = resolveUserLevel(userId, levelKor);

        List<NewsOrigin> origins = newsQueryRepository.findByStatus(NewsStatus.AUTO_CRAWLED);

        List<NewsDTO> dtos = origins.stream()
                .map(origin -> {
                    NewsTransformed transformed = newsTransformedRepository
                            .findByNewsIdAndLevel(origin.getId(), level)
                            .orElse(null);

                    boolean isBookmarked = userId != null &&
                            savedNewsRepository.existsByUserIdAndNewsOriginIdAndSavedLevel(userId, origin.getId(), level);

                    return NewsConverter.toNewsDto(origin, transformed, isBookmarked);
                })
                .collect(Collectors.toList());

        return ApiResponse.success(dtos, ResponseCode.COMMON_SUCCESS);
    }

    @Override
    public ApiResponse<NewsDetailDTO> getNewsDetail(Long newsId, Long userId, String levelKor) {
        TransformLevel level = resolveUserLevel(userId, levelKor);

        NewsOrigin origin = newsQueryRepository.findById(newsId)
                .orElseThrow(() -> new NewsException(ResponseCode.NEWS_NOT_FOUND));

        NewsTransformed transformed = newsTransformedRepository
                .findByNewsIdAndLevel(newsId, level)
                .orElseThrow(() -> new NewsException(ResponseCode.NEWS_NOT_FOUND));

        boolean isBookmarked = userId != null &&
                savedNewsRepository.existsByUserIdAndNewsOriginIdAndSavedLevel(userId, newsId, level);

        List<Word> words = wordRepository.findByNews(origin);

        List<NewsDetailDTO.KeywordDto> keywordDtos = words.stream()
                .map(word -> NewsDetailDTO.KeywordDto.builder()
                        .wordId(word.getWordId())
                        .term(word.getTerm())
                        .description(word.getDescription())
                        .source(origin.getSource())
                        .build())
                .collect(Collectors.toList());

        NewsDetailDTO dto = NewsConverter.toNewsDetailDto(
                origin,
                transformed,
                level.getKorean(),
                isBookmarked,
                keywordDtos
        );

        return ApiResponse.success(dto, ResponseCode.COMMON_SUCCESS);
    }

    @Override
    public ApiResponse<List<NewsDTO>> searchNews(String keyword, Long userId, String levelKor) {
        TransformLevel level = resolveUserLevel(userId, levelKor);

        List<NewsTransformed> transformedList =
                newsTransformedRepository.searchByLevelAndKeywordAndStatus(
                        level, keyword, NewsStatus.AUTO_CRAWLED
                );

        List<NewsDTO> dtos = transformedList.stream()
                .map(transformed -> {
                    NewsOrigin origin = transformed.getNews();

                    boolean isBookmarked = userId != null &&
                            savedNewsRepository.existsByUserIdAndNewsOriginIdAndSavedLevel(
                                    userId,
                                    origin.getId(),
                                    level
                            );

                    return NewsConverter.toNewsDto(origin, transformed, isBookmarked);
                })
                .collect(Collectors.toList());

        return ApiResponse.success(dtos, ResponseCode.NEWS_SEARCH_SUCCESS);
    }



    @Override
    public ApiResponse<?> saveNews(Long userId, Long newsId, String levelKor) {
        if (userId == null) {
            throw new NewsException(ResponseCode.USER_UNAUTHORIZED);
        }

        TransformLevel level = getUserCurrentLevel(userId);

        NewsOrigin origin = newsQueryRepository.findById(newsId)
                .orElseThrow(() -> new NewsException(ResponseCode.NEWS_NOT_FOUND));

        boolean exists = savedNewsRepository.existsByUserIdAndNewsOriginIdAndSavedLevel(userId, newsId, level);
        if (exists) {
            throw new NewsException(ResponseCode.NEWS_ALREADY_SAVED);
        }

        SavedNews savedNews = SavedNews.builder()
                .userId(userId)
                .newsOrigin(origin)
                .savedLevel(level)
                .build();

        savedNewsRepository.save(savedNews);

        return ApiResponse.success(
                new SaveNewsResponseDTO(userId, newsId),
                ResponseCode.NEWS_SAVE_SUCCESS
        );
    }

    @Override
    public ApiResponse<List<SavedNewsDTO>> getSavedNews(Long userId, String levelKor) {
        List<SavedNews> saved = savedNewsRepository.findByUserId(userId);

        List<SavedNewsDTO> dtos = saved.stream()
                .map(savedNews -> {
                    NewsOrigin origin = savedNews.getNewsOrigin();
                    TransformLevel savedLevel = savedNews.getSavedLevel();

                    NewsTransformed transformed = newsTransformedRepository
                            .findByNewsIdAndLevel(origin.getId(), savedLevel)
                            .orElse(null);

                    return NewsConverter.toSavedNewsDto(savedNews, transformed);
                })
                .collect(Collectors.toList());

        return ApiResponse.success(dtos, ResponseCode.COMMON_SUCCESS);
    }


    @Override
    public ApiResponse<?> deleteNewsBookmark(Long userId, Long savedNewsId) {
        SavedNews savedNews = savedNewsRepository.findById(savedNewsId)
                .orElseThrow(() -> new NewsException(ResponseCode.NEWS_NOT_FOUND));

        if (!savedNews.getUserId().equals(userId)) {
            throw new NewsException(ResponseCode.USER_UNAUTHORIZED);
        }

        savedNewsRepository.delete(savedNews);

        return ApiResponse.success(null, ResponseCode.COMMON_SUCCESS);
    }


    private TransformLevel resolveUserLevel(Long userId, String levelKor) {
        if (levelKor != null) {
            return TransformLevel.fromKorean(levelKor);
        }
        if (userId == null) {
            return TransformLevel.MEDIUM;
        }
        return getUserCurrentLevel(userId);
    }

    private TransformLevel getUserCurrentLevel(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NewsException(ResponseCode.USER_NOT_FOUND));

        Level userLevel = user.getLevel();

        if (userLevel == null) {
            return TransformLevel.MEDIUM;
        }

        return switch (userLevel) {
            case HIGH -> TransformLevel.HARD;
            case MEDIUM -> TransformLevel.MEDIUM;
            case LOW -> TransformLevel.EASY;
        };
    }
}
