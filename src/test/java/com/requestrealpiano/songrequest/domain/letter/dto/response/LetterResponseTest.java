package com.requestrealpiano.songrequest.domain.letter.dto.response;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.dto.response.inner.AccountSummary;
import com.requestrealpiano.songrequest.domain.letter.dto.response.inner.SongSummary;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.testobject.AccountFactory;
import com.requestrealpiano.songrequest.testobject.LetterFactory;
import com.requestrealpiano.songrequest.testobject.SongFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LetterResponseTest {

    @Test
    @DisplayName("Letter를 통해 Letter DTO를 생성하는 테스트")
    void create_new_letter_response() {
        // given
        Letter letter = LetterFactory.createOne();
        Song song = letter.getSong();
        Account account = letter.getAccount();

        // when
        LetterResponse letterResponse = LetterResponse.from(letter);

        // then
        assertAll(
                () -> assertThat(letterResponse.getSongStory()).isEqualTo(letter.getSongStory()),
                () -> assertThat(letterResponse.getRequestStatus()).isEqualTo(letter.getRequestStatus().getKey()),
                () -> assertThat(letterResponse.getSong()).isEqualToComparingFieldByField(SongSummary.from(song)),
                () -> assertThat(letterResponse.getAccount()).isEqualToComparingFieldByField(AccountSummary.from(account))
        );
    }
}
