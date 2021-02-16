package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.domain.account.Role;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.LetterRepository;
import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import com.requestrealpiano.songrequest.domain.letter.dto.request.NewLetterRequest;
import com.requestrealpiano.songrequest.domain.letter.dto.request.NewLetterRequestBuilder;
import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequestBuilder;
import com.requestrealpiano.songrequest.domain.letter.dto.response.LetterResponse;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.global.error.LetterNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.requestrealpiano.songrequest.testobject.AccountFactory.createMember;
import static com.requestrealpiano.songrequest.testobject.LetterFactory.*;
import static com.requestrealpiano.songrequest.testobject.SongFactory.createSong;
import static com.requestrealpiano.songrequest.testobject.SongFactory.createSongRequest;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    @Test
    @DisplayName("저장 되어 있는 모든 Letters 로부터 LetterResponses 를 생성하는 테스트")
    void find_all_letters() {
        // given
        Account account = createMember();
        Song song = createSong();
        List<Letter> letters = createLettersOf(account, song);

        // when
        when(letterRepository.findAll()).thenReturn(letters);
        List<LetterResponse> letterResponses = letterService.findAllLetters();

        // then
        assertThat(letterResponses.size()).isEqualTo(letters.size());
    }

    @Test
    @DisplayName("정상적인 ID로부터 Letter를 조회하는 테스트")
    void find_letter_by_valid_id() {
        // given
        Long validId = 1L;
        Letter letter = createLetter();
        LetterResponse testResponse = LetterResponse.from(letter);

        // when
        when(letterRepository.findById(validId)).thenReturn(Optional.of(letter));
        LetterResponse letterResponse = letterService.findLetter(validId);

        // then
        assertThat(letterResponse.getSongStory()).isEqualTo(testResponse.getSongStory());
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
        assertThatThrownBy(() -> letterService.findLetter(invalidId)).isInstanceOf(LetterNotFoundException.class);
    }

    @Test
    @DisplayName("새로운 Letter를 생성하는 테스트")
    void create_new_letter() {
        // given
        NewLetterRequest newLetterRequest = createNewLetterRequest();

        String songStory = newLetterRequest.getSongStory();
        Account account = createMember();
        Song song = createSong();
        Letter newLetter = Letter.of(songStory, account, song);

        // when
        when(accountRepository.findById(eq(newLetterRequest.getAccountId()))).thenReturn(Optional.of(account));
        when(letterRepository.save(any(Letter.class))).thenReturn(newLetter);
        LetterResponse letterResponse = letterService.createNewLetter(newLetterRequest);

        // then
        assertAll(
                () -> assertThat(letterResponse.getSongStory()).isEqualTo(newLetter.getSongStory()),
                () -> assertThat(letterResponse.getRequestStatus()).isEqualTo(newLetter.getRequestStatus().getKey()),
                () -> assertThat(letterResponse.getSong().getTitle()).isEqualTo(newLetter.getSong().getSongTitle()),
                () -> assertThat(letterResponse.getAccount().getName()).isEqualTo(newLetter.getAccount().getName()),
                () -> assertThat(letterResponse.getAccount().getAvatarUrl()).isEqualTo(newLetter.getAccount().getAvatarUrl())
        );
    }
}
