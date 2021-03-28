package com.requestrealpiano.songrequest.domain.letter.response;

import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.response.inner.LetterDetails;
import com.requestrealpiano.songrequest.global.pagination.response.PageDetails;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LettersResponse {

    private final PageDetails pageDetails;
    private final List<LetterDetails> letters;

    @Builder
    private LettersResponse(PageDetails pageDetails, List<LetterDetails> letters) {
        this.pageDetails = pageDetails;
        this.letters = letters;
    }

    public static LettersResponse from(Page<Letter> currentPageLetters) {
        List<Letter> currentLetters = currentPageLetters.getContent();
        List<LetterDetails> letters = currentLetters.stream()
                                                    .map(LetterDetails::from)
                                                    .collect(Collectors.toList());

        return LettersResponse.builder()
                              .pageDetails(PageDetails.from(currentPageLetters))
                              .letters(letters)
                              .build();
    }
}