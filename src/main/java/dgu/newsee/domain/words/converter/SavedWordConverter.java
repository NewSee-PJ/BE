package dgu.newsee.domain.words.converter;

import dgu.newsee.domain.words.entity.SavedWord;
import dgu.newsee.domain.words.entity.Word;
import dgu.newsee.domain.words.dto.SavedWordDTO.WordResponse;

import java.util.List;

public class SavedWordConverter {

    public static WordResponse.SaveResult toSaveResult(SavedWord savedWord, Word word) {
        return WordResponse.SaveResult.builder()
                .savedWordId(savedWord.getSavedWordId())
                .wordId(word.getWordId())
                .term(word.getTerm())
                .description(word.getDescription())
                .category(word.getCategory())
                .date(savedWord.getDate().toString())
                .build();
    }

    public static WordResponse.WordDetail toWordDetail(SavedWord savedWord, Word word) {
        return WordResponse.WordDetail.builder()
                .savedWordId(savedWord.getSavedWordId())
                .wordId(word.getWordId())
                .term(word.getTerm())
                .description(word.getDescription())
                .category(word.getCategory())
                .date(savedWord.getDate().toString())
                .build();
    }

    public static WordResponse.SavedWordListResponse toSavedWordListResponse(List<WordResponse.WordDetail> details) {
        return WordResponse.SavedWordListResponse.builder()
                .totalCount(details.size())
                .words(details)
                .build();
    }

    public static WordResponse.DeleteResult toDeleteResult(Long savedWordId) {
        return WordResponse.DeleteResult.builder()
                .deletedSavedWordId(savedWordId)
                .build();
    }
}
