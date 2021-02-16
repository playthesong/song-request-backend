package com.requestrealpiano.songrequest.testobject;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import com.requestrealpiano.songrequest.domain.letter.dto.request.NewLetterRequest;
import com.requestrealpiano.songrequest.domain.letter.dto.request.NewLetterRequestBuilder;
import com.requestrealpiano.songrequest.domain.song.Song;

import java.util.Arrays;
import java.util.List;

public class LetterFactory {

    /*
     *
     * createMockObject()
     *   - Test parameter 에 의존하지 않는 테스트 객체 생성
     *     (ex. Mocking 에서 자주 사용되는 테스트 객체)
     *
     *
     * createMockObjectOf(T parameter1, T parameter2, ...)
     *   - Test parameter 에 의존하는 테스트 객체 생성
     *     (ex. 예외 검증, 경우의 수를 따져야 하는 테스트)
     */

    // Letter
    public static Letter createLetter() {
        return Letter.builder()
                     .songStory("Song story")
                     .requestStatus(RequestStatus.WAITING)
                     .song(SongFactory.createSong())
                     .account(AccountFactory.createMember())
                     .build();
    }

    // Letters
    public static List<Letter> createLetters() {
        Letter firstLetter = Letter.builder()
                                   .songStory("Song Story 1")
                                   .requestStatus(RequestStatus.WAITING)
                                   .build();
        Letter secondLetter = Letter.builder()
                                    .songStory("Song Story 2")
                                    .requestStatus(RequestStatus.DONE)
                                    .build();
        Letter thirdLetter = Letter.builder()
                                   .songStory("Song Story 3")
                                   .requestStatus(RequestStatus.PENDING)
                                   .build();
        return Arrays.asList(firstLetter, secondLetter, thirdLetter);
    }

    public static List<Letter> createLettersOf(Account account, Song song) {
        Letter firstLetter = Letter.builder()
                                .songStory("Song Story 1")
                                .requestStatus(RequestStatus.WAITING)
                                .account(account)
                                .song(song)
                                .build();
        Letter secondLetter = Letter.builder()
                                    .songStory("Song Story 2")
                                    .requestStatus(RequestStatus.DONE)
                                    .account(account)
                                    .song(song)
                                    .build();
        Letter thirdLetter = Letter.builder()
                                   .songStory("Song Story 3")
                                   .requestStatus(RequestStatus.PENDING)
                                   .account(account)
                                   .song(song)
                                   .build();
        return Arrays.asList(firstLetter, secondLetter, thirdLetter);
    }

    // NewLetterRequest
    public static NewLetterRequest createNewLetterRequest() {
        return NewLetterRequestBuilder.newBuilder()
                                      .songStory("Song Story")
                                      .songRequest(SongFactory.createSongRequest())
                                      .accountId(1L)
                                      .build();
    }
}
