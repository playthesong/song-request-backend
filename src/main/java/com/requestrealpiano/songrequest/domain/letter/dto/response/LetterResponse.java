package com.requestrealpiano.songrequest.domain.letter.dto.response;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import com.requestrealpiano.songrequest.domain.letter.dto.response.inner.AccountSummary;
import com.requestrealpiano.songrequest.domain.letter.dto.response.inner.SongSummary;
import com.requestrealpiano.songrequest.domain.song.Song;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LetterResponse {

    private final Long id;
    private final String songStory;
    private final String requestStatus;
    private final LocalDateTime createdDateTime;
    private final SongSummary song;
    private final AccountSummary account;

    @Builder
    private LetterResponse(Long id, String songStory, String requestStatus, LocalDateTime createdDateTime,
                           SongSummary songSummary, AccountSummary accountSummary) {
        this.id = id;
        this.songStory = songStory;
        this.requestStatus = requestStatus;
        this.createdDateTime = createdDateTime;
        this.song = songSummary;
        this.account = accountSummary;
    }

    public static LetterResponse from(Letter letter) {
        Song song = letter.getSong();
        Account account = letter.getAccount();
        RequestStatus requestStatus = letter.getRequestStatus();

        return LetterResponse.builder()
                                   .id(letter.getId())
                                   .songStory(letter.getSongStory())
                                   .requestStatus(requestStatus.getKey())
                                   .createdDateTime(letter.getCreatedDateTime())
                                   .songSummary(SongSummary.from(song))
                                   .accountSummary(AccountSummary.from(account))
                                   .build();
    }
}
