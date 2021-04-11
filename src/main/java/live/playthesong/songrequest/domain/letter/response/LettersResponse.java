package live.playthesong.songrequest.domain.letter.response;

import live.playthesong.songrequest.domain.letter.Letter;
import live.playthesong.songrequest.domain.letter.response.inner.LetterDetails;
import live.playthesong.songrequest.global.pagination.response.PageDetails;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LettersResponse {

    private final PageDetails pageDetails;
    private final boolean readyToLetter;
    private final List<LetterDetails> letters;

    @Builder
    private LettersResponse(PageDetails pageDetails, List<LetterDetails> letters, boolean readyToLetter) {
        this.pageDetails = pageDetails;
        this.readyToLetter = readyToLetter;
        this.letters = letters;
    }

    public static LettersResponse from(Page<Letter> currentPageLetters, boolean readyToLetter) {
        List<Letter> currentLetters = currentPageLetters.getContent();
        List<LetterDetails> letters = currentLetters.stream()
                                                    .map(LetterDetails::from)
                                                    .collect(Collectors.toList());

        return LettersResponse.builder()
                              .pageDetails(PageDetails.from(currentPageLetters))
                              .readyToLetter(readyToLetter)
                              .letters(letters)
                              .build();
    }
}
