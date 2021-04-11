package live.playthesong.songrequest.domain.letter.response;

import live.playthesong.songrequest.domain.account.Account;
import live.playthesong.songrequest.domain.letter.Letter;
import live.playthesong.songrequest.domain.letter.response.inner.AccountSummary;
import live.playthesong.songrequest.domain.letter.response.inner.LetterDetails;
import live.playthesong.songrequest.domain.letter.response.inner.SongSummary;
import live.playthesong.songrequest.domain.song.Song;
import live.playthesong.songrequest.testobject.LetterFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LetterDetailsTest {

    @Test
    @DisplayName("Letter를 통해 Letter DTO를 생성하는 테스트")
    void create_new_letter_response() {
        // given
        Letter letter = LetterFactory.createLetter();
        Song song = letter.getSong();
        Account account = letter.getAccount();

        // when
        LetterDetails letterDetails = LetterDetails.from(letter);

        // then
        assertAll(
                () -> assertThat(letterDetails.getSongStory()).isEqualTo(letter.getSongStory()),
                () -> assertThat(letterDetails.getRequestStatus()).isEqualTo(letter.getRequestStatus().getKey()),
                () -> assertThat(letterDetails.getSong()).isEqualToComparingFieldByField(SongSummary.from(song)),
                () -> assertThat(letterDetails.getAccount()).isEqualToComparingFieldByField(AccountSummary.from(account))
        );
    }
}
