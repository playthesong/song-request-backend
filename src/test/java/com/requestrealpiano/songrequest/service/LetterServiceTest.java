package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.LetterRepository;
import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import com.requestrealpiano.songrequest.domain.letter.dto.response.LetterResponse;
import com.requestrealpiano.songrequest.domain.song.Song;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LetterServiceTest {

    @InjectMocks
    LetterService letterService;

    @Mock
    LetterRepository letterRepository;

    @DisplayName("저장 되어 있는 모든 Letters 로부터 LetterResponses 를 생성하는 테스트")
    @Test
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
}
