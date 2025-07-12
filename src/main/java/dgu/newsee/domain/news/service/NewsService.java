package dgu.newsee.domain.news.service;

import dgu.newsee.domain.news.dto.NewsDetailDTO;
import dgu.newsee.domain.news.dto.NewsDTO;
import dgu.newsee.domain.news.dto.SavedNewsDTO;
import dgu.newsee.global.payload.ApiResponse;

import java.util.List;

public interface NewsService {

    ApiResponse<List<NewsDTO>> getAllNews(Long userId, String levelKor);

    ApiResponse<NewsDetailDTO> getNewsDetail(Long newsId, Long userId, String levelKor);

    ApiResponse<List<NewsDTO>> searchNews(String keyword, Long userId, String levelKor);

    ApiResponse<List<SavedNewsDTO>> getSavedNews(Long userId, String levelKor);

    ApiResponse<?> saveNews(Long userId, Long newsId, String levelKor);

    ApiResponse<?> deleteNewsBookmark(Long userId, Long savedNewsId);

}