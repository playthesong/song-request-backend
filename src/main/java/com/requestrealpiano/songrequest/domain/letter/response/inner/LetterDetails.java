package com.requestrealpiano.songrequest.domain.letter.response.inner;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import com.requestrealpiano.songrequest.domain.song.Song;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LetterDetails {

    private final Long id;
    private final String songStory;
    private final String requestStatus;
    private final LocalDateTime createdDateTime;
    private final SongSummary song;
    private final AccountSummary account;

    @Builder
    private LetterDetails(Long id, String songStory, String requestStatus, LocalDateTime createdDateTime,
                          SongSummary songSummary, AccountSummary accountSummary) {
        this.id = id;
        this.songStory = songStory;
        this.requestStatus = requestStatus;
        this.createdDateTime = createdDateTime;
        this.song = songSummary;
        this.account = accountSummary;
    }

    public static LetterDetails from(Letter letter) {
        Song song = letter.getSong();
        Account account = letter.getAccount();
        RequestStatus requestStatus = letter.getRequestStatus();

        return LetterDetails.builder()
                             .id(letter.getId())
                             .songStory(letter.getSongStory())
                             .requestStatus(requestStatus.getKey())
                             .createdDateTime(letter.getCreatedDateTime())
                             .songSummary(SongSummary.from(song))
                             .accountSummary(AccountSummary.from(account))
                             .build();
    }
}
