package com.requestrealpiano.songrequest.domain.song;

import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.youtubecontent.YoutubeContent;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_id")
    private Long id;

    @Column(name = "song_title")
    private String songTitle;

    @Column(name = "album_title")
    private String albumTitle;

    private String artist;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "request_count")
    private Integer requestCount;

    @OneToMany(mappedBy ="song")
    private List<Letter> letters = new ArrayList<>();

    @OneToMany(mappedBy = "song")
    private List<YoutubeContent> youtubeContent = new ArrayList<>();

    @Builder
    private Song(Long id, String songTitle, String albumTitle, String artist, String imageUrl, Integer requestCount) {
        this.id = id;
        this.songTitle = songTitle;
        this.albumTitle = albumTitle;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.requestCount = requestCount;
    }

    public Song increaseRequestCount() {
        this.requestCount += 1;
        return this;
    }

    public static Song from(SongRequest songRequest) {
        return Song.builder()
                   .songTitle(songRequest.getTitle())
                   .artist(songRequest.getArtist())
                   .imageUrl(songRequest.getImageUrl())
                   .build();
    }

    @PrePersist
    public void prePersist() {
        // 1 - Song 데이터 생성시 RequestCount 기본 값
        if (this.requestCount == null) {
            this.requestCount = 1;
        }
    }
}
