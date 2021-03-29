package com.requestrealpiano.songrequest.domain.letter;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.Role;
import com.requestrealpiano.songrequest.domain.letter.request.LetterRequest;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.security.oauth.OAuthAccount;
import com.requestrealpiano.songrequest.testobject.AccountFactory;
import com.requestrealpiano.songrequest.testobject.SongFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.requestrealpiano.songrequest.domain.account.Role.MEMBER;
import static com.requestrealpiano.songrequest.testobject.AccountFactory.*;
import static com.requestrealpiano.songrequest.testobject.LetterFactory.*;
import static com.requestrealpiano.songrequest.testobject.SongFactory.*;
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
    @MethodSource("hasSameAccountParameters")
    @DisplayName("일치 - Letter의 Account와 LoginAccount 일치여부 확인 테스트")
    void same_has_different_account(Long letterAccountId, Long loginAccountId) {
        // given
        Account letterAccount = createMemberOf(letterAccountId);
        OAuthAccount loginAccount = createOAuthAccountOf(loginAccountId, MEMBER);
        Letter letter = createLetterOf(letterAccount, createSong());

        // when
        boolean different = letter.hasDifferentAccount(loginAccount);

        // then
        assertThat(different).isFalse();
    }

    @ParameterizedTest
    @MethodSource("hasDifferentAccountParameters")
    @DisplayName("불일치 - Letter의 Account와 LoginAccount 일치여부 확인 테스트")
    void not_same_has_different_account(Long letterAccountId, Long loginAccountId) {
        // given
        Account letterAccount = createMemberOf(letterAccountId);
        OAuthAccount loginAccount = createOAuthAccountOf(loginAccountId, MEMBER);
        Letter letter = createLetterOf(letterAccount, createSong());

        // when
        boolean different = letter.hasDifferentAccount(loginAccount);

        // then
        assertThat(different).isTrue();
    }

    @ParameterizedTest
    @MethodSource("hasSameSongParameters")
    @DisplayName("일치 - Letter의 Song과 SongRequest 일치여부 확인 테스트")
    void has_same_song(String letterArtist, String requestArtist, String letterTitle, String requestTitle,
                       String imageUrl) {
        // given
        Song song = createSongOf(letterTitle, letterArtist, imageUrl);
        Letter letter = createLetterOf(createMember(), song);
        SongRequest songRequest = createSongRequestOf(requestTitle, requestArtist, imageUrl);

        // when
        boolean same = letter.hasSameSong(songRequest);

        // then
        assertThat(same).isTrue();
    }

    @ParameterizedTest
    @MethodSource("hasNotSameSongParameters")
    @DisplayName("불일치 - Letter의 Song과 SongRequest 일치여부 확인 테스트")
    void has_not_same_song(String letterArtist, String requestArtist, String letterTitle, String requestTitle,
                       String imageUrl) {
        // given
        Song song = createSongOf(letterTitle, letterArtist, imageUrl);
        Letter letter = createLetterOf(createMember(), song);
        SongRequest songRequest = createSongRequestOf(requestTitle, requestArtist, imageUrl);

        // when
        boolean same = letter.hasSameSong(songRequest);

        // then
        assertThat(same).isFalse();
    }

    @ParameterizedTest
    @MethodSource("changeSongStoryParameters")
    @DisplayName("songStory change 메서드 테스트")
    void change_song_story(String changedSongStory) {
        // given
        Letter letter = createLetter();

        // when
        Letter updatedLetter = letter.changeSongStory(changedSongStory);

        // then
        assertThat(updatedLetter.getSongStory()).isEqualTo(changedSongStory);
    }

    private static Stream<Arguments> changeSongStoryParameters() {
        return Stream.of(
                Arguments.of("Changed Song Story")
        );
    }

    @Test
    @DisplayName("Song Change 메서드 테스트")
    void change_song() {
        // given
        int first = 0;

        Song song = createSong();
        List<Letter> letters = createLettersOf(createMember(), song);
        song.getLetters().addAll(letters);

        Letter updateLetter = letters.get(first);

        // when
        Song newSong = createSong();
        updateLetter.changeSong(newSong);

        // then
        assertThat(song.getLetters()
                        .stream()
                        .filter(letter -> letter.getId().equals(updateLetter.getId()))
                        .count()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("updateLetterParameters")
    @DisplayName("Letter Update 메서드 테스트")
    void update_letter(Long songId, Long newSongId, String newSongStory) {
        // given
        Song song = createSongOf(songId);
        Letter letter = createLetterOf(createMember(), song);
        SongRequest songRequest = createSongRequestOf(song.getSongTitle(), song.getArtist(), song.getImageUrl());
        LetterRequest letterRequest = createLetterRequestOf(newSongStory, songRequest);

        // when
        Song newSong = createSongOf(newSongId);
        Letter updatedLetter = letter.update(letterRequest, newSong);

        // then
        assertAll(
                () -> assertThat(updatedLetter.getSongStory()).isEqualTo(newSongStory),
                () -> assertThat(updatedLetter.getSong().getId()).isNotEqualTo(songId),
                () -> assertThat(updatedLetter.getSong().getId()).isEqualTo(newSongId)
        );
    }

    private static Stream<Arguments> updateLetterParameters() {
        return Stream.of(
                Arguments.of(1L, 2L, "New Song Story")
        );
    }

    private static Stream<Arguments> hasSameSongParameters() {
        return Stream.of(
                Arguments.of("SameArtist", "SameArtist", "SameTitle", "SameTitle", "http://imageUrl")
        );
    }

    private static Stream<Arguments> hasNotSameSongParameters() {
        return Stream.of(
                Arguments.of("NotSameArtist", "NotSame Artist", "SameTitle", "SameTitle", "http://imageUrl"),
                Arguments.of("SameArtist", "SameArtist", "NotSameTitle", "NotSame Title", "http://imageUrl"),
                Arguments.of("NotSameArtist", "NotSame Artist", "NotSame Title", "NotSameTitle", "http://imageUrl")
        );
    }

    private static Stream<Arguments> hasSameAccountParameters() {
        return Stream.of(
                Arguments.of(1L, 1L)
        );
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
