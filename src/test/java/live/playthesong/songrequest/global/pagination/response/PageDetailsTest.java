package live.playthesong.songrequest.global.pagination.response;

import live.playthesong.songrequest.domain.letter.Letter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import static live.playthesong.songrequest.testobject.LetterFactory.createLettersPage;
import static org.assertj.core.api.Assertions.assertThat;

class PageDetailsTest {

    @Test
    @DisplayName("정적 팩토리 메서드 from으로부터 PageDetails를 생성하는 테스트")
    void create_pagedetails_from() {
        // given
        Page<Letter> letters = createLettersPage();

        // when
        PageDetails pageDetails = PageDetails.from(letters);

        // then
        assertThat(pageDetails).hasNoNullFieldsOrProperties();
    }
}
