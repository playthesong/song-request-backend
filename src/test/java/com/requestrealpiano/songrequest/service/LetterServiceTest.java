package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.LetterRepository;
import com.requestrealpiano.songrequest.domain.letter.request.NewLetterRequest;
import com.requestrealpiano.songrequest.domain.letter.request.PaginationParameters;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.letter.response.LettersResponse;
import com.requestrealpiano.songrequest.domain.letter.response.inner.LetterDetails;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.global.error.exception.business.LetterNotFoundException;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import com.requestrealpiano.songrequest.global.time.Scheduler;
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

import static com.requestrealpiano.songrequest.domain.letter.RequestStatus.WAITING;
import static com.requestrealpiano.songrequest.testobject.AccountFactory.createMember;
import static com.requestrealpiano.songrequest.testobject.LetterFactory.*;
import static com.requestrealpiano.songrequest.testobject.PaginationFactory.createPageRequest;
import static com.requestrealpiano.songrequest.testobject.PaginationFactory.createPaginationParameters;
import static com.requestrealpiano.songrequest.testobject.SongFactory.createSong;
import static com.requestrealpiano.songrequest.testobject.SongFactory.createSongRequest;
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
        NewLetterRequest newLetterRequest = createNewLetterRequestOf(songStory, songRequest, accountId);

        Account account = createMember();
        Song song = createSong();
        Letter newLetter = Letter.of(songStory, account, song);

        // when
        when(accountRepository.findById(eq(newLetterRequest.getAccountId()))).thenReturn(Optional.of(account));
        when(letterRepository.save(any(Letter.class))).thenReturn(newLetter);
        LetterDetails letterDetails = letterService.createLetter(newLetterRequest);

        // then
        assertAll(
                () -> assertThat(letterDetails.getSongStory()).isEqualTo(newLetter.getSongStory()),
                () -> assertThat(letterDetails.getRequestStatus()).isEqualTo(newLetter.getRequestStatus().getKey()),
                () -> assertThat(letterDetails.getSong().getTitle()).isEqualTo(newLetter.getSong().getSongTitle()),
                () -> assertThat(letterDetails.getAccount().getName()).isEqualTo(newLetter.getAccount().getName()),
                () -> assertThat(letterDetails.getAccount().getAvatarUrl()).isEqualTo(newLetter.getAccount().getAvatarUrl())
        );
    }

    private static Stream<Arguments> createNewLetterParameters() {
        return Stream.of(
                Arguments.of("Song story", createSongRequest(), 1L)
        );
    }
}
