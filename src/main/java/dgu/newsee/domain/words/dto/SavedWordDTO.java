package dgu.newsee.domain.words.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SavedWordDTO {

    public static class WordResponse {

        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class WordDetail {
            private Long savedWordId;
            private Long wordId;
            private String term;
            private String description;
            private String category;
            private String date;
        }

        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class SavedWordListResponse {
            private int totalCount;
            private List<WordDetail> words;
        }

        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class SaveResult {
            private Long savedWordId;
            private Long wordId;
            private String term;
            private String description;
            private String category;
            private String date;
        }

        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class DeleteResult {
            private Long deletedSavedWordId;
        }
    }
}