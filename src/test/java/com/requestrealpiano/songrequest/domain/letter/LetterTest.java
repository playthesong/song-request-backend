package com.requestrealpiano.songrequest.domain.letter;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.Role;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.security.oauth.OAuthAccount;
import com.requestrealpiano.songrequest.testobject.AccountFactory;
import com.requestrealpiano.songrequest.testobject.SongFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.requestrealpiano.songrequest.domain.account.Role.MEMBER;
import static com.requestrealpiano.songrequest.testobject.AccountFactory.*;
import static com.requestrealpiano.songrequest.testobject.LetterFactory.createLetterOf;
import static com.requestrealpiano.songrequest.testobject.SongFactory.createSong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LetterTest {

    @ParameterizedTest
    @MethodSource("createNewLetterByOfParameters")
    @DisplayName("정적 메서드 of()로부터 새로운 Letter를 생성하는 테스트")
    void create_new_letter_by_of(String songStory, RequestStatus defaultStatus) {
        // given
        Account account = createMember();
        Song song = createSong();

        // when
        Letter letter = Letter.of(songStory, account, song);

        // then
        assertAll(
                () -> assertThat(letter.getSongStory()).isEqualTo(songStory),
                () -> assertThat(letter.getRequestStatus()).isEqualTo(defaultStatus),
                () -> assertThat(letter.getAccount()).isEqualTo(account),
                () -> assertThat(letter.getSong()).isEqualTo(song)
        );
    }

    @ParameterizedTest
    @MethodSource("hasDifferentAccountParameters")
    @DisplayName("Letter의 Account와 LoginAccount 일치여부 확인 테스트")
    void has_different_account(Long letterAccountId, Long loginAccountId) {
        // given
        Account letterAccount = createMemberOf(letterAccountId);
        OAuthAccount loginAccount = createOAuthAccountOf(loginAccountId, MEMBER);
        Letter letter = createLetterOf(letterAccount, createSong());

        // when
        boolean different = letter.hasDifferentAccount(loginAccount);

        // then
        assertThat(different).isTrue();
    }

    private static Stream<Arguments> hasDifferentAccountParameters() {
        return Stream.of(
                Arguments.of(1L, 2L),
                Arguments.of(5L, 1L),
                Arguments.of(3L, 5L)
        );
    }

    private static Stream<Arguments> createNewLetterByOfParameters() {
        return Stream.of(
                Arguments.of("Song story", RequestStatus.WAITING)
        );
    }
}
