package com.requestrealpiano.songrequest.service.letter;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.LetterRepository;
import com.requestrealpiano.songrequest.domain.letter.request.LetterRequest;
import com.requestrealpiano.songrequest.domain.letter.request.PaginationParameters;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.letter.response.LettersResponse;
import com.requestrealpiano.songrequest.domain.letter.response.inner.LetterDetails;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.global.error.exception.BusinessException;
import com.requestrealpiano.songrequest.global.error.exception.business.AccountMismatchException;
import com.requestrealpiano.songrequest.global.error.exception.business.LetterNotFoundException;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import com.requestrealpiano.songrequest.global.time.Scheduler;
import com.requestrealpiano.songrequest.security.oauth.OAuthAccount;
import com.requestrealpiano.songrequest.service.LetterService;
import com.requestrealpiano.songrequest.service.SongService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.requestrealpiano.songrequest.domain.account.Role.MEMBER;
import static com.requestrealpiano.songrequest.domain.letter.RequestStatus.WAITING;
import static com.requestrealpiano.songrequest.testobject.AccountFactory.*;
import static com.requestrealpiano.songrequest.testobject.LetterFactory.*;
import static com.requestrealpiano.songrequest.testobject.PaginationFactory.createPageRequest;
import static com.requestrealpiano.songrequest.testobject.PaginationFactory.createPaginationParameters;
import static com.requestrealpiano.songrequest.testobject.SongFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LetterServiceTest {

    @InjectMocks
    LetterService letterService;

    @Mock
    LetterRepository letterRepository;

    @Mock
    AccountRepository accountRepository;

    @Mock
    SongService songService;

    @Mock
    Scheduler scheduler;

    @Test
    @DisplayName("저장 되어 있는 모든 Letters 로부터 LetterResponses 를 생성하는 테스트")
    void find_all_letters() {
        // given
        PaginationParameters parameters = createPaginationParameters();
        PageRequest pageRequest = createPageRequest();
        Account account = createMember();
        Song song = createSong();
        List<Letter> letters = createLettersOf(account, song);

        // when
        LocalDateTime now = LocalDateTime.now();
        when(scheduler.now()).thenReturn(now);
        when(scheduler.defaultStartDateTimeFrom((any(LocalDateTime.class)))).thenReturn(now.minusDays(1));
        when(letterRepository.findAllTodayLetters(refEq(pageRequest), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new PageImpl<>(letters));
        LettersResponse lettersResponse = letterService.findAllLetters(parameters);

        // then
        assertThat(lettersResponse.getLetters().size()).isEqualTo(letters.size());
    }

    @Test
    @DisplayName("특정 RequestStatus의 Letter 리스트를 받아와서 LetterResponse 리스트로 반환하는 테스트")
    void find_all_letters_by_request_status() {
        // given
        int first = 0;
        PaginationParameters paginationParameters = createPaginationParameters();
        PageRequest pageRequest = createPageRequest();
        List<Letter> waitingLetters = Collections.singletonList(createLetter());

        // when
        LocalDateTime now = LocalDateTime.now();
        when(scheduler.now()).thenReturn(now);
        when(scheduler.defaultStartDateTimeFrom((any(LocalDateTime.class)))).thenReturn(now.minusDays(1));
        when(letterRepository.findAllTodayLettersByRequestStatus(refEq(pageRequest), eq(WAITING),
                                                                 any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new PageImpl<>(waitingLetters));
        LettersResponse waitingLettersResponse = letterService.findLettersByStatus(WAITING, paginationParameters);

        Letter waitingLetter = waitingLetters.get(first);
        LetterDetails waitingLetterDetails = waitingLettersResponse.getLetters().get(first);

        // then
        assertAll(
                () -> assertThat(waitingLetterDetails.getRequestStatus()).isEqualTo(waitingLetter.getRequestStatus().getKey()),
                () -> assertThat(waitingLetterDetails.getAccount().getAvatarUrl()).isEqualTo(waitingLetter.getAccount().getAvatarUrl()),
                () -> assertThat(waitingLetterDetails.getSong().getTitle()).isEqualTo(waitingLetter.getSong().getSongTitle()),
                () -> assertThat(waitingLetterDetails.getSongStory()).isEqualTo(waitingLetter.getSongStory())
        );
    }

    @Test
    @DisplayName("정상적인 ID로부터 Letter를 조회하는 테스트")
    void find_letter_by_valid_id() {
        // given
        Long validId = 1L;
        Letter letter = createLetter();
        LetterDetails testResponse = LetterDetails.from(letter);

        // when
        when(letterRepository.findById(validId)).thenReturn(Optional.of(letter));
        LetterDetails letterDetails = letterService.findLetter(validId);

        // then
        assertThat(letterDetails.getSongStory()).isEqualTo(testResponse.getSongStory());
        assertThatCode(() -> letterService.findLetter(validId)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("LetterNotFoundException - 존재하지 않는 ID 값으로 Letter 를 조회할 때 예외가 발생하는 테스트")
    void find_letter_by_invalid_id() {
        // given
        Long invalidId = 1234567L;

        // when
        when(letterRepository.findById(invalidId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> letterService.findLetter(invalidId)).isInstanceOf(LetterNotFoundException.class)
                                                                     .hasMessage(ErrorCode.LETTER_NOT_FOUND.getMessage());
    }

    @ParameterizedTest
    @MethodSource("createNewLetterParameters")
    @DisplayName("새로운 Letter를 생성하는 테스트")
    void create_new_letter(String songStory, SongRequest songRequest, Long accountId) {
        // given
        LetterRequest letterRequest = createLetterRequestOf(songStory, songRequest);
        OAuthAccount loginAccount = createOAuthAccountOf(accountId, MEMBER);
        Account account = createMember();
        Song song = createSong();
        Letter newLetter = Letter.of(songStory, account, song);

        // when
        when(accountRepository.findById(eq(loginAccount.getId()))).thenReturn(Optional.of(account));
        when(letterRepository.save(any(Letter.class))).thenReturn(newLetter);
        LetterDetails letterDetails = letterService.createLetter(loginAccount, letterRequest);

        // then
        assertAll(
                () -> assertThat(letterDetails.getSongStory()).isEqualTo(newLetter.getSongStory()),
                () -> assertThat(letterDetails.getRequestStatus()).isEqualTo(newLetter.getRequestStatus().getKey()),
                () -> assertThat(letterDetails.getSong().getTitle()).isEqualTo(newLetter.getSong().getSongTitle()),
                () -> assertThat(letterDetails.getAccount().getName()).isEqualTo(newLetter.getAccount().getName()),
                () -> assertThat(letterDetails.getAccount().getAvatarUrl()).isEqualTo(newLetter.getAccount().getAvatarUrl())
        );
    }

    @ParameterizedTest
    @MethodSource("sameSongUpdateLetterParameters")
    @DisplayName("Same Song - Song이 같을 때 update 테스트")
    void same_song_update_letter(Long accountId, Long songId, String artist, String title, String newSongStory) {
        // given
        Song song = createSongOf(songId, artist, title);
        SongRequest songRequest = createSongRequestOf(song.getSongTitle(), song.getArtist(), song.getImageUrl());

        Letter letter = createLetterOf(createMemberOf(accountId), song);
        Account account = letter.getAccount();

        OAuthAccount loginAccount = createOAuthAccountOf(account.getId(), account.getRole());
        LetterRequest letterRequest = createLetterRequestOf(newSongStory, songRequest);

        // when
        when(letterRepository.findById(eq(letter.getId()))).thenReturn(Optional.of(letter));

        LetterDetails updatedLetter = letterService.updateLetter(loginAccount, letter.getId(), letterRequest);

        // then
        assertAll(
                () -> assertThat(updatedLetter.getId()).isEqualTo(letter.getId()),
                () -> assertThat(updatedLetter.getAccount().getId()).isEqualTo(loginAccount.getId()),
                () -> assertThat(updatedLetter.getSong().getId()).isEqualTo(song.getId()),
                () -> assertThat(updatedLetter.getSongStory()).isEqualTo(newSongStory)
        );
    }

    @ParameterizedTest
    @MethodSource("notSameSongUpdateLetterParameters")
    @DisplayName("Not Same - Song이 다를 때 update 테스트")
    void not_same_song_update_letter(Long accountId, Long songId, Long newSongId, String artist, String title,
                                     String newArtist, String newTitle, String newSongStory) {
        // given
        Song oldSong = createSongOf(songId, artist, title);
        Letter letter = createLetterOf(createMemberOf(accountId), oldSong);

        Account account = letter.getAccount();
        OAuthAccount loginAccount = createOAuthAccountOf(account.getId(), account.getRole());

        Song newSong = createSongOf(newSongId, newTitle, newArtist);
        SongRequest songRequest = createSongRequestOf(newSong.getSongTitle(), newSong.getArtist(), newSong.getImageUrl());
        LetterRequest letterRequest = createLetterRequestOf(newSongStory, songRequest);

        // when
        when(letterRepository.findById(eq(letter.getId()))).thenReturn(Optional.of(letter));
        when(songService.updateRequestCountOrElseCreate(refEq(letterRequest.getSongRequest()))).thenReturn(newSong);
        LetterDetails updatedLetter = letterService.updateLetter(loginAccount, letter.getId(), letterRequest);

        // then
        assertAll(
                () -> assertThat(updatedLetter.getId()).isEqualTo(letter.getId()),
                () -> assertThat(updatedLetter.getAccount().getId()).isEqualTo(loginAccount.getId()),
                () -> assertThat(updatedLetter.getSong().getId()).isNotEqualTo(oldSong.getId()),
                () -> assertThat(updatedLetter.getSong().getId()).isEqualTo(newSong.getId()),
                () -> assertThat(updatedLetter.getSong().getTitle()).isEqualTo(newSong.getSongTitle()),
                () -> assertThat(updatedLetter.getSong().getArtist()).isEqualTo(newSong.getArtist()),
                () -> assertThat(updatedLetter.getSongStory()).isEqualTo(newSongStory)
        );
    }

    @ParameterizedTest
    @MethodSource("accountMismatchUpdateLetterParameters")
    @DisplayName("Account Mismatch - Login Account가 일치하지 않을 때 예외가 발생하는 테스트")
    void account_mismatch_update_letter(Long letterAccountId, Long differentId, String songStory) {
        // given
        ErrorCode accountMismatchError = ErrorCode.ACCOUNT_MISMATCH_ERROR;
        LetterRequest letterRequest = createLetterRequestOf(songStory, createSongRequest());
        Letter letter = createLetterOf(createMemberOf(letterAccountId), createSong());
        OAuthAccount loginAccount = createOAuthAccountOf(differentId, MEMBER);

        // when
        when(letterRepository.findById(eq(letter.getId()))).thenReturn(Optional.of(letter));

        // then
        assertThatThrownBy(() -> letterService.updateLetter(loginAccount, letter.getId(), letterRequest))
                .isExactlyInstanceOf(AccountMismatchException.class)
                .isInstanceOf(BusinessException.class)
                .hasMessage(accountMismatchError.getMessage());
    }

    private static Stream<Arguments> accountMismatchUpdateLetterParameters() {
        return Stream.of(
                Arguments.of(1L, 3L, "SongStory")
        );
    }

    private static Stream<Arguments> notSameSongUpdateLetterParameters() {
        return Stream.of(
                Arguments.of(5L, 3L, 10L, "Original Artist", "Original Title",
                            "New Artist", "New Title", "NewSongStory")
        );
    }

    private static Stream<Arguments> sameSongUpdateLetterParameters() {
        return Stream.of(
                Arguments.of(5L, 3L, "SameArtist", "SameTitle", "NewSongStory")
        );
    }

    private static Stream<Arguments> createNewLetterParameters() {
        return Stream.of(
                Arguments.of("Song story", createSongRequest(), 1L)
        );
    }
}
