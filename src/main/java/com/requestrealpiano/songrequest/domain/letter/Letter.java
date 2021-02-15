package com.requestrealpiano.songrequest.domain.letter;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.base.BaseTimeEntity;
import com.requestrealpiano.songrequest.domain.song.Song;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Letter extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "letter_id")
    private Long id;

    @Column(name = "song_story")
    private String songStory;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    private RequestStatus requestStatus;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;

    @Builder
    private Letter(String songStory, RequestStatus requestStatus, Account account, Song song) {
        this.songStory = songStory;
        this.requestStatus = requestStatus;
        this.account = account;
        this.song = song;
    }

    public static Letter of(String songStory, Account account, Song song) {
        return Letter.builder()
                     .songStory(songStory)
                     .requestStatus(RequestStatus.WAITING)
                     .account(account)
                     .song(song)
                     .build();
    }
}
