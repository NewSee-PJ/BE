package dgu.newsee.domain.words.service;

import dgu.newsee.domain.words.converter.SavedWordConverter;
import dgu.newsee.domain.words.entity.SavedWord;
import dgu.newsee.domain.words.entity.Word;
import dgu.newsee.global.exception.UserException;
import dgu.newsee.global.exception.SavedWordException;
import dgu.newsee.domain.words.dto.SavedWordDTO.WordResponse;
import dgu.newsee.domain.words.repository.SavedWordRepository;
import dgu.newsee.domain.words.repository.WordRepository;
import dgu.newsee.global.payload.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SavedWordServiceImpl implements SavedWordService {

    private final WordRepository wordRepository;
    private final SavedWordRepository savedWordRepository;

    @Override
    public WordResponse.SaveResult saveWord(String userId, Long wordId) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new SavedWordException(ResponseCode.WORD_NOT_FOUND));

        boolean alreadyExists = savedWordRepository
                .findByUserIdAndWordId(Long.valueOf(userId), wordId)
                .isPresent();

        if (alreadyExists) {
            throw new SavedWordException(ResponseCode.WORD_ALREADY_SAVED);
        }

        SavedWord savedWord = savedWordRepository.save(
                SavedWord.builder()
                        .userId(Long.valueOf(userId))
                        .wordId(wordId)
                        .date(LocalDate.now())
                        .build()
        );

        return SavedWordConverter.toSaveResult(savedWord, word);
    }

    @Override
    public WordResponse.SavedWordListResponse getAllWords(String userId) {
        List<SavedWord> savedWords = savedWordRepository.findByUserId(Long.valueOf(userId));
        List<WordResponse.WordDetail> details = new ArrayList<>();

        for (SavedWord savedWord : savedWords) {
            Word word = wordRepository.findById(savedWord.getWordId())
                    .orElseThrow(() -> new SavedWordException(ResponseCode.WORD_NOT_FOUND));
            details.add(SavedWordConverter.toWordDetail(savedWord, word));
        }

        return WordResponse.SavedWordListResponse.builder()
                .totalCount(details.size())
                .words(details)
                .build();
    }

    @Override
    public WordResponse.SavedWordListResponse searchWords(String userId, String keyword) {
        List<Word> words;

        if (keyword == null || keyword.isEmpty()) {
            words = wordRepository.findAll();
        } else {
            words = wordRepository.findByTermContainingOrDescriptionContaining(keyword, keyword);
        }

        List<WordResponse.WordDetail> result = new ArrayList<>();

        for (Word word : words) {
            savedWordRepository.findByUserIdAndWordId(Long.valueOf(userId), word.getWordId())
                    .ifPresent(savedWord ->
                            result.add(SavedWordConverter.toWordDetail(savedWord, word))
                    );
        }

        return SavedWordConverter.toSavedWordListResponse(result);
    }

    @Override
    public WordResponse.DeleteResult deleteWord(String userId, Long savedWordId) {
        SavedWord savedWord = savedWordRepository.findById(savedWordId)
                .orElseThrow(() -> new SavedWordException(ResponseCode.WORD_NOT_FOUND));

        if (!savedWord.getUserId().equals(Long.valueOf(userId))) {
            throw new UserException(ResponseCode.USER_FORBIDDEN);
        }

        savedWordRepository.delete(savedWord);

        return SavedWordConverter.toDeleteResult(savedWordId);
    }
}