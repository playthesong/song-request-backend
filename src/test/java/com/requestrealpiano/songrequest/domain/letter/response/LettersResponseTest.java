package com.requestrealpiano.songrequest.domain.letter.response;

import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.global.pagination.response.PageDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import static com.requestrealpiano.songrequest.testobject.LetterFactory.createLettersPage;
import static com.requestrealpiano.songrequest.testobject.PaginationFactory.createPageDetailsOf;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LettersResponseTest {

    @Test
    @DisplayName("정적 팩토리 메서드 from으로부터 LettersResponse를 생성하는 테스트")
    void create_lettersresponse_from() {
        // given
        Page<Letter> lettersPage = createLettersPage();
        PageDetails pageDetails = createPageDetailsOf(lettersPage);

        // when
        LettersResponse lettersResponse = LettersResponse.from(lettersPage, TRUE);

        // then
        assertAll(
                () -> assertThat(lettersResponse.getPageDetails()).isEqualToComparingFieldByField(pageDetails),
                () -> assertThat(lettersResponse.isReadyToLetter()).isTrue(),
                () -> assertThat(lettersResponse.getLetters().size()).isEqualTo(lettersPage.getContent().size())
        );
    }
}
