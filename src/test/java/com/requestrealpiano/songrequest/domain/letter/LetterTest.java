package com.requestrealpiano.songrequest.domain.letter;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.testobject.AccountFactory;
import com.requestrealpiano.songrequest.testobject.SongFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LetterTest {

    @ParameterizedTest
    @MethodSource("createNewLetterByOfParameters")
    @DisplayName("정적 메서드 of()로부터 새로운 Letter를 생성하는 테스트")
    void create_new_letter_by_of(String songStory) {
        // given
        Account account = AccountFactory.createMember();
        Song song = SongFactory.createOne();

        // when
        Letter letter = Letter.of(songStory, account, song);

        // then
        assertAll(
                () -> assertThat(letter.getSongStory()).isEqualTo(songStory),
                () -> assertThat(letter.getRequestStatus()).isEqualTo(RequestStatus.WAITING),
                () -> assertThat(letter.getAccount()).isEqualTo(account),
                () -> assertThat(letter.getSong()).isEqualTo(song)
        );
    }

    private static Stream<Arguments> createNewLetterByOfParameters() {
        return Stream.of(
                Arguments.of("Song story")
        );
    }
}
