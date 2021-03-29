package com.requestrealpiano.songrequest.domain.letter;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.base.BaseTimeEntity;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.security.oauth.OAuthAccount;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

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
    private Letter(Long id, String songStory, RequestStatus requestStatus, Account account, Song song) {
        this.id = id;
        this.songStory = songStory;
        this.requestStatus = requestStatus;
        this.account = account;
        this.song = song;
    }

    public boolean hasDifferentAccount(OAuthAccount loginAccount) {
        Long loginId = loginAccount.getId();
        return !loginId.equals(this.getAccount().getId());
    }

    public boolean hasSameSong(SongRequest songRequest) {
        boolean sameArtist = songRequest.getArtist().equals(song.getArtist());
        boolean sameTitle = songRequest.getTitle().equals(song.getSongTitle());
        return sameArtist && sameTitle;
    }

    public Letter changeSongStory(String songStory) {
        this.songStory = songStory;
        return this;
    }

    public void changeSong(Song song) {
        this.song.getLetters().removeIf(letter -> letter.getId().equals(id));
        this.song = song;
    }

    public Letter update(String songStory, Song song) {
        changeSongStory(songStory);
        changeSong(song);
        return this;
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
