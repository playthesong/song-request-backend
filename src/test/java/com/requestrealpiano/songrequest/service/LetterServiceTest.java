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
        List<Letter> letters = createMockLetters();
        List<LetterResponse> testResponses = letters.stream()
                                                    .map(LetterResponse::from)
                                                    .collect(Collectors.toList());

        // when
        when(letterRepository.findAll()).thenReturn(letters);
        List<LetterResponse> letterResponses = letterService.findAllLetters();

        // then
        assertThat(letterResponses.size()).isEqualTo(testResponses.size());
    }

    private List<Letter> createMockLetters() {
        Letter firstLetter = Letter.builder()
                .songStory("Song Story 1")
                .requestStatus(RequestStatus.WAITING)
                .song(Song.builder().build())
                .account(Account.builder().build())
                .build();
        Letter secondLetter = Letter.builder()
                .songStory("Song Story 2")
                .requestStatus(RequestStatus.WAITING)
                .song(Song.builder().build())
                .account(Account.builder().build())
                .build();
        Letter thirdLetter = Letter.builder()
                .songStory("Song Story 3")
                .requestStatus(RequestStatus.WAITING)
                .song(Song.builder().build())
                .account(Account.builder().build())
                .build();
        return Arrays.asList(firstLetter, secondLetter, thirdLetter);
    }

    @ParameterizedTest
    @MethodSource("findLetterByValidIdParameters")
    @DisplayName("정상적인 ID로부터 Letter를 조회하는 테스트")
    void find_letter_by_valid_id(Long validId, String songStory, RequestStatus requestStatus) {
        // given
        Letter letter = Letter.builder()
                              .songStory(songStory)
                              .requestStatus(requestStatus)
                              .account(Account.builder().build())
                              .song(Song.builder().build())
                              .build();
        LetterResponse testResponse = LetterResponse.from(letter);

        // when
        when(letterRepository.findById(validId)).thenReturn(Optional.of(letter));
        LetterResponse letterResponse = letterService.findLetter(validId);

        // then
        assertThat(letterResponse.getSongStory()).isEqualTo(testResponse.getSongStory());
        assertThatCode(() -> letterService.findLetter(validId)).doesNotThrowAnyException();
    }

    private static Stream<Arguments> findLetterByValidIdParameters() {
        return Stream.of(
                Arguments.of(1L, "Song Story", RequestStatus.WAITING)
        );
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

    @ParameterizedTest
    @MethodSource("createNewLetterParameters")
    @DisplayName("새로운 Letter를 생성하는 테스트")
    void create_new_letter(String title, String artist, String imageUrl, String songStory, Long accountId,
                           Long googleOauthId, String name, String email, Role role, String avatarUrl, Integer requestCount) {
        // given
        SongRequest songRequest = SongRequestBuilder.newBuilder()
                                                    .title(title)
                                                    .artist(artist)
                                                    .imageUrl(imageUrl)
                                                    .build();
        NewLetterRequest newLetterRequest = NewLetterRequestBuilder.newBuilder()
                                                                   .songStory(songStory)
                                                                   .songRequest(songRequest)
                                                                   .accountId(accountId)
                                                                   .build();
        Song song = Song.builder()
                        .songTitle(title)
                        .artist(artist)
                        .imageUrl(imageUrl)
                        .build();
        Account account = Account.builder()
                                 .googleOauthId(googleOauthId)
                                 .name(name)
                                 .email(email)
                                 .role(role)
                                 .avatarUrl(avatarUrl)
                                 .requestCount(requestCount)
                                 .build();
        Letter letter = Letter.of(songStory, account, song);


        // when
        when(accountRepository.findById(eq(newLetterRequest.getAccountId()))).thenReturn(Optional.of(account));
        when(letterRepository.save(any(Letter.class))).thenReturn(letter);
        LetterResponse letterResponse = letterService.createNewLetter(newLetterRequest);

        // then
        assertAll(
                () -> assertThat(letterResponse.getSongStory()).isEqualTo(songStory),
                () -> assertThat(letterResponse.getRequestStatus()).isEqualTo(RequestStatus.WAITING.getKey()),
                () -> assertThat(letterResponse.getSong().getTitle()).isEqualTo(title),
                () -> assertThat(letterResponse.getAccount().getName()).isEqualTo(name),
                () -> assertThat(letterResponse.getAccount().getAvatarUrl()).isEqualTo(avatarUrl)
        );
    }

    private static Stream<Arguments> createNewLetterParameters() {
        return Stream.of(
                Arguments.of("Song title", "Artist", "imageUrl", "Song Story", 1L, 1234567L, "Username", "User email",
                             Role.MEMBER, "http://avatarUrl", 1)
        );
    }
}
