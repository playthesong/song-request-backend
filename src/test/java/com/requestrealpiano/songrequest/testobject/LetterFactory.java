package com.requestrealpiano.songrequest.testobject;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import com.requestrealpiano.songrequest.domain.letter.request.NewLetterRequest;
import com.requestrealpiano.songrequest.domain.letter.request.NewLetterRequestBuilder;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.letter.response.LetterResponse;
import com.requestrealpiano.songrequest.domain.letter.response.inner.AccountSummary;
import com.requestrealpiano.songrequest.domain.letter.response.inner.SongSummary;
import com.requestrealpiano.songrequest.domain.song.Song;

import java.time.LocalDateTime;
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

    // LetterResponse
    public static LetterResponse createLetterResponse() {
        return LetterResponse.builder()
                             .id(1L)
                             .songStory("Song Story")
                             .requestStatus(RequestStatus.WAITING.getKey())
                             .createdDateTime(LocalDateTime.of(2021, 2, 14, 8, 43, 1))
                             .accountSummary(AccountSummary.builder()
                                                           .id(1L)
                                                           .name("Username")
                                                           .avatarUrl("http://avatarUrl")
                                                           .build())
                             .songSummary(SongSummary.builder()
                                                     .id(1L)
                                                     .title("Song Title")
                                                     .artist("Artist")
                                                     .imageUrl("http://imageUrl")
                                                     .build())
                             .build();
    }

    // LetterResponses
    public static List<LetterResponse> createLetterResponses() {
        LetterResponse firstLetterResponse = LetterResponse.builder()
                                                            .id(1L)
                                                            .songStory("Song Story 1")
                                                            .requestStatus(RequestStatus.WAITING.getKey())
                                                            .createdDateTime(LocalDateTime.of(2021, 2, 14, 8, 43, 1))
                                                            .accountSummary(AccountSummary.builder()
                                                                                          .id(1L)
                                                                                          .name("Name 1")
                                                                                          .avatarUrl("http://avatarUrl_1")
                                                                                          .build())
                                                            .songSummary(SongSummary.builder()
                                                                                    .id(1L)
                                                                                    .title("Song Title 1")
                                                                                    .artist("Artist 1")
                                                                                    .imageUrl("http://imageUrl_1")
                                                                                    .build())
                                                            .build();

        LetterResponse secondLetterResponse = LetterResponse.builder()
                                                            .id(2L)
                                                            .songStory("Song Story 2")
                                                            .requestStatus(RequestStatus.WAITING.getKey())
                                                            .createdDateTime(LocalDateTime.of(2021, 2, 14, 8, 43, 1))
                                                            .accountSummary(AccountSummary.builder()
                                                                                          .id(2L)
                                                                                          .name("Name 2")
                                                                                          .avatarUrl("http://avatarUrl_2")
                                                                                          .build())
                                                            .songSummary(SongSummary.builder()
                                                                                    .id(2L)
                                                                                    .title("Song Title 2")
                                                                                    .artist("Artist 2")
                                                                                    .imageUrl("http://imageUrl_2")
                                                                                    .build())
                                                            .build();

        LetterResponse thirdLetterResponse = LetterResponse.builder()
                                                           .id(3L)
                                                           .songStory("Song Story 3")
                                                           .requestStatus(RequestStatus.WAITING.getKey())
                                                           .createdDateTime(LocalDateTime.of(2021, 2, 14, 8, 43, 1))
                                                           .accountSummary(AccountSummary.builder()
                                                                                         .id(3L)
                                                                                         .name("Name 3")
                                                                                         .avatarUrl("http://avatarUrl_3")
                                                                                         .build())
                                                           .songSummary(SongSummary.builder()
                                                                                   .id(3L)
                                                                                   .title("Song Title 3")
                                                                                   .artist("Artist 3")
                                                                                   .imageUrl("http://imageUrl_3")
                                                                                   .build())
                                                           .build();
        return Arrays.asList(firstLetterResponse, secondLetterResponse, thirdLetterResponse);
    }

    // NewLetterRequest
    public static NewLetterRequest createNewLetterRequestOf(String songStory, SongRequest songRequest, Long accountId) {
        return NewLetterRequestBuilder.newBuilder()
                                      .songStory(songStory)
                                      .songRequest(songRequest)
                                      .accountId(accountId)
                                      .build();
    }
}
