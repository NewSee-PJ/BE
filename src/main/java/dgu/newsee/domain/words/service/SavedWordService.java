package dgu.newsee.domain.words.service;

import dgu.newsee.domain.words.dto.SavedWordDTO.WordResponse;

public interface SavedWordService {
    WordResponse.SaveResult saveWord(String userId, Long wordId);
    WordResponse.SavedWordListResponse getAllWords(String userId);
    WordResponse.SavedWordListResponse searchWords(String userId, String keyword);
    WordResponse.DeleteResult deleteWord(String userId, Long savedWordId);
}