package com.requestrealpiano.songrequest.domain.letter.dto.response;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.dto.response.inner.AccountSummary;
import com.requestrealpiano.songrequest.domain.letter.dto.response.inner.SongSummary;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LetterResponse {

    private final Long id;
    private final String songStory;
    private final String requestStatus;
    private final SongSummary song;
    private final AccountSummary account;

    @Builder
    private LetterResponse(Long id, String songStory, String requestStatus,
                           SongSummary songSummary, AccountSummary accountSummary) {
        this.id = id;
        this.songStory = songStory;
        this.requestStatus = requestStatus;
        this.song = songSummary;
        this.account = accountSummary;
    }

    public static LetterResponse from(Letter letter) {
        Song song = letter.getSong();
        Account account = letter.getAccount();
        RequestStatus requestStatus = letter.getRequestStatus();

        return LetterResponse.builder()
                                   .id(letter.getId())
                                   .songSummary(SongSummary.from(song))
                                   .requestStatus(requestStatus.getKey())
                                   .accountSummary(AccountSummary.from(account))
                                   .build();
    }
}
