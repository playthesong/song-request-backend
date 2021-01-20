package com.requestrealpiano.songrequest.domain.songrequest.dto.response;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.domain.songrequest.RequestStatus;
import com.requestrealpiano.songrequest.domain.songrequest.SongRequest;
import com.requestrealpiano.songrequest.domain.songrequest.dto.response.inner.AccountSummary;
import com.requestrealpiano.songrequest.domain.songrequest.dto.response.inner.SongSummary;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SongRequestsResponse {

    private final Long id;
    private final String songStory;
    private final String requestStatus;
    private final SongSummary song;
    private final AccountSummary account;

    @Builder
    private SongRequestsResponse(Long id, String songStory, String requestStatus,
                                 SongSummary songSummary, AccountSummary accountSummary) {
        this.id = id;
        this.songStory = songStory;
        this.requestStatus = requestStatus;
        this.song = songSummary;
        this.account = accountSummary;
    }

    public static SongRequestsResponse from(SongRequest songRequest) {
        Song song = songRequest.getSong();
        Account account = songRequest.getAccount();
        RequestStatus requestStatus = songRequest.getRequestStatus();

        return SongRequestsResponse.builder()
                                   .id(songRequest.getId())
                                   .songSummary(SongSummary.from(song))
                                   .requestStatus(requestStatus.getKey())
                                   .accountSummary(AccountSummary.from(account))
                                   .build();
    }
}
