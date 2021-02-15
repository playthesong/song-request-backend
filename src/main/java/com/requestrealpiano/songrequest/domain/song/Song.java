package com.requestrealpiano.songrequest.domain.song;

import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequest;
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
    private int requestCount;

    @OneToMany(mappedBy ="song")
    private List<Letter> letters = new ArrayList<>();

    @OneToMany(mappedBy = "song")
    private List<YoutubeContent> youtubeContent = new ArrayList<>();

    @Builder
    private Song(String songTitle, String albumTitle, String artist, String imageUrl, int requestCount) {
        this.songTitle = songTitle;
        this.albumTitle = albumTitle;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.requestCount = requestCount;
    }

    public Song increaseRequestCount() {
        int increase = 1;
        this.requestCount += increase;
        return this;
    }

    public static Song from(SongRequest songRequest) {
        int initialCount = 1;

        return Song.builder()
                   .songTitle(songRequest.getTitle())
                   .artist(songRequest.getArtist())
                   .imageUrl(songRequest.getImageUrl())
                   .requestCount(initialCount)
                   .build();
    }
}
