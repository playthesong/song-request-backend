package com.requestrealpiano.songrequest.domain.letter;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.song.Song;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LetterTest {

    @ParameterizedTest
    @MethodSource("createNewLetterParameters")
    @DisplayName("새로운 Letter를 생성할 수 있다.")
    void create_new_letter(String songStory, RequestStatus requestStatus, Account account, Song song) {
        // given
        Letter letter = Letter.builder()
                              .songStory(songStory)
                              .requestStatus(requestStatus)
                              .account(account)
                              .song(song)
                              .build();

        // then
        assertAll(
                () -> assertThat(letter).isNotNull(),
                () -> assertThat(letter.getSongStory()).isEqualTo(songStory),
                () -> assertThat(letter.getRequestStatus()).isEqualTo(requestStatus),
                () -> assertThat(letter.getCreatedDateTime()).isNotNull(),
                () -> assertThat(letter.getAccount()).isEqualTo(account),
                () -> assertThat(letter.getSong()).isEqualTo(song)
        );
    }

    private static Stream<Arguments> createNewLetterParameters() {
        return Stream.of(
                Arguments.of("사연 1 입니다.", RequestStatus.WAITING,
                             Account.builder().name("Name1").build(),
                             Song.builder().songTitle("노래 제목1").build()),
                Arguments.of("사연 2 입니다.", RequestStatus.PENDING,
                             Account.builder().name("Name2").build(),
                             Song.builder().songTitle("노래 제목2").build()),
                Arguments.of("사연 1 입니다.", RequestStatus.WAITING,
                             Account.builder().name("Name3").build(),
                             Song.builder().songTitle("노래 제목3").build())
        );
    }
}
