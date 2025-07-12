package dgu.newsee.domain.news.converter;

import dgu.newsee.domain.crawlednews.entity.NewsOrigin;
import dgu.newsee.domain.news.dto.NewsDetailDTO;
import dgu.newsee.domain.news.dto.NewsDTO;
import dgu.newsee.domain.news.dto.SavedNewsDTO;
import dgu.newsee.domain.news.entity.SavedNews;
import dgu.newsee.domain.transformednews.entity.NewsTransformed;

import java.util.List;

public class NewsConverter {

    public static NewsDTO toNewsDto(NewsOrigin origin, NewsTransformed transformed, boolean isBookmarked) {
        return NewsDTO.builder()
                .newsId(origin.getId())
                .title(transformed.getNews().getTitle())  // transformed 제목
                .category(origin.getCategory())
                .transformedContent(transformed.getTransformedContent()) // transformed 내용
                .source(origin.getSource())
                .time(origin.getTime().toString())
                .url(origin.getOriginalUrl())
                .imageUrl(origin.getImageUrl())
                .isBookmarked(isBookmarked)
                .build();
    }

    public static SavedNewsDTO toSavedNewsDto(
            SavedNews savedNews,
            NewsTransformed transformed
    ) {
        NewsOrigin origin = savedNews.getNewsOrigin();
        return SavedNewsDTO.builder()
                .savedNewsId(savedNews.getId())
                .newsId(origin.getId())
                .title(transformed != null ? transformed.getNews().getTitle() : origin.getTitle())
                .category(origin.getCategory())
                .source(origin.getSource())
                .time(origin.getTime().toString())
                .url(origin.getOriginalUrl())
                .imageUrl(origin.getImageUrl())
                .transformedContent(transformed != null ? transformed.getTransformedContent() : null)
                .build();
    }

    public static NewsDetailDTO toNewsDetailDto(
            NewsOrigin origin,
            NewsTransformed transformed,
            String userLevel,
            boolean isBookmarked,
            List<NewsDetailDTO.KeywordDto> keywords
    ) {
        return NewsDetailDTO.builder()
                .newsId(origin.getId())
                .title(transformed.getNews().getTitle())
                .category(origin.getCategory())
                .source(origin.getSource())
                .time(origin.getTime().toString())
                .url(origin.getOriginalUrl())
                .imageUrl(origin.getImageUrl())
                .isBookmarked(isBookmarked)
                .transformedContent(transformed.getTransformedContent())
                .summary(transformed.getSummarized())
                .keywords(keywords)
                .build();
    }
}