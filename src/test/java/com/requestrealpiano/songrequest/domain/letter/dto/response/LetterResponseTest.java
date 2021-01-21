package com.requestrealpiano.songrequest.domain.letter.dto.response;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import com.requestrealpiano.songrequest.domain.letter.dto.response.inner.AccountSummary;
import com.requestrealpiano.songrequest.domain.letter.dto.response.inner.SongSummary;
import com.requestrealpiano.songrequest.domain.song.Song;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LetterResponseTest {

    @ParameterizedTest
    @MethodSource("createNewLetterResponseParameters")
    @DisplayName("Letter를 통해 Letter DTO를 생성하는 테스트")
    void create_new_letter_response(String songStory, RequestStatus requestStatus, Song song, Account account) {
        // given
        Letter letter = Letter.builder()
                              .songStory(songStory)
                              .requestStatus(requestStatus)
                              .song(song)
                              .account(account)
                              .build();

        // when
        LetterResponse letterResponse = LetterResponse.from(letter);

        // then
        assertAll(
                () -> assertThat(letterResponse.getSongStory()).isEqualTo(songStory),
                () -> assertThat(letterResponse.getRequestStatus()).isEqualTo(requestStatus.getKey()),
                () -> assertThat(letterResponse.getSong()).isEqualToComparingFieldByField(SongSummary.from(song)),
                () -> assertThat(letterResponse.getAccount()).isEqualToComparingFieldByField(AccountSummary.from(account))
        );
    }

    private static Stream<Arguments> createNewLetterResponseParameters() {
        return Stream.of(
                Arguments.of("Song Story 1", RequestStatus.WAITING,
                        Song.builder().songTitle("Song Title 1").build(),
                        Account.builder().name("Account Name 1").build()),
                Arguments.of("Song Story 2", RequestStatus.PENDING,
                        Song.builder().songTitle("Song Title 2").build(),
                        Account.builder().name("Account Name 2").build()),
                Arguments.of("Song Story 1", RequestStatus.WAITING,
                        Song.builder().songTitle("Song Title 3").build(),
                        Account.builder().name("Account Name 3").build())
        );
    }
}
