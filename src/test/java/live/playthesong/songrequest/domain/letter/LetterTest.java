package live.playthesong.songrequest.domain.letter;

import live.playthesong.songrequest.domain.account.Account;
import live.playthesong.songrequest.domain.letter.request.LetterRequest;
import live.playthesong.songrequest.domain.letter.request.StatusChangeRequest;
import live.playthesong.songrequest.domain.letter.request.inner.SongRequest;
import live.playthesong.songrequest.domain.song.Song;
import live.playthesong.songrequest.security.oauth.OAuthAccount;
import live.playthesong.songrequest.domain.account.Role;
import live.playthesong.songrequest.testobject.AccountFactory;
import live.playthesong.songrequest.testobject.LetterFactory;
import live.playthesong.songrequest.testobject.SongFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LetterTest {

    @ParameterizedTest
    @MethodSource("createNewLetterByOfParameters")
    @DisplayName("정적 메서드 of()로부터 새로운 Letter를 생성하는 테스트")
    void create_new_letter_by_of(String songStory, RequestStatus defaultStatus) {
        // given
        Account account = AccountFactory.createMember();
        Song song = SongFactory.createSong();

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
        Account letterAccount = AccountFactory.createMemberOf(letterAccountId);
        OAuthAccount loginAccount = AccountFactory.createOAuthAccountOf(loginAccountId, Role.MEMBER);
        Letter letter = LetterFactory.createLetterOf(letterAccount, SongFactory.createSong());

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
        Account letterAccount = AccountFactory.createMemberOf(letterAccountId);
        OAuthAccount loginAccount = AccountFactory.createOAuthAccountOf(loginAccountId, Role.MEMBER);
        Letter letter = LetterFactory.createLetterOf(letterAccount, SongFactory.createSong());

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
        Song song = SongFactory.createSongOf(letterTitle, letterArtist, imageUrl);
        Letter letter = LetterFactory.createLetterOf(AccountFactory.createMember(), song);
        SongRequest songRequest = SongFactory.createSongRequestOf(requestTitle, requestArtist, imageUrl);

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
        Song song = SongFactory.createSongOf(letterTitle, letterArtist, imageUrl);
        Letter letter = LetterFactory.createLetterOf(AccountFactory.createMember(), song);
        SongRequest songRequest = SongFactory.createSongRequestOf(requestTitle, requestArtist, imageUrl);

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
        Letter letter = LetterFactory.createLetter();

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

        Song song = SongFactory.createSong();
        List<Letter> letters = LetterFactory.createLettersOf(AccountFactory.createMember(), song);
        song.getLetters().addAll(letters);

        Letter updateLetter = letters.get(first);

        // when
        Song newSong = SongFactory.createSong();
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
        Song song = SongFactory.createSongOf(songId);
        Letter letter = LetterFactory.createLetterOf(AccountFactory.createMember(), song);
        SongRequest songRequest = SongFactory.createSongRequestOf(song.getSongTitle(), song.getArtist(), song.getImageUrl());
        LetterRequest letterRequest = LetterFactory.createLetterRequestOf(newSongStory, songRequest);

        // when
        Song newSong = SongFactory.createSongOf(newSongId);
        Letter updatedLetter = letter.update(letterRequest, newSong);

        // then
        assertAll(
                () -> assertThat(updatedLetter.getSongStory()).isEqualTo(newSongStory),
                () -> assertThat(updatedLetter.getSong().getId()).isNotEqualTo(songId),
                () -> assertThat(updatedLetter.getSong().getId()).isEqualTo(newSongId)
        );
    }

    @ParameterizedTest
    @MethodSource("changeRequestStatusParameters")
    @DisplayName("Letter 상태 변경 메서드 테스트")
    void change_request_status(RequestStatus letterStatus, StatusChangeRequest statusChangeRequest) {
        // given
        Letter letter = LetterFactory.createLetterOf(letterStatus, AccountFactory.createMember(), SongFactory.createSong());

        // when
        Letter updatedLetter = letter.changeStatus(statusChangeRequest);

        // then
        assertThat(updatedLetter.getRequestStatus()).isEqualTo(statusChangeRequest.getRequestStatus());
    }

    private static Stream<Arguments> changeRequestStatusParameters() {
        StatusChangeRequest changeToWaiting = LetterFactory.createStatusChangeRequestOf(RequestStatus.WAITING);
        StatusChangeRequest changeToPending = LetterFactory.createStatusChangeRequestOf(RequestStatus.PENDING);
        StatusChangeRequest changeToDone = LetterFactory.createStatusChangeRequestOf(RequestStatus.DONE);
        return Stream.of(
                Arguments.of(RequestStatus.WAITING, changeToDone), Arguments.of(RequestStatus.PENDING, changeToWaiting),
                Arguments.of(RequestStatus.WAITING, changeToPending), Arguments.of(RequestStatus.PENDING, changeToDone),
                Arguments.of(RequestStatus.DONE, changeToWaiting),
                Arguments.of(RequestStatus.DONE, changeToPending)
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
