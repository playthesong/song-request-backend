package com.requestrealpiano.songrequest.domain.song;

import com.requestrealpiano.songrequest.domain.songrequest.SongRequest;
import com.requestrealpiano.songrequest.domain.youtubecontent.YoutubeContent;
import lombok.AccessLevel;
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

    @Column(name = "hash_id")
    private String hashId;

    @OneToMany(mappedBy ="song")
    private List<SongRequest> songRequests = new ArrayList<>();

    @OneToMany(mappedBy = "song")
    private List<YoutubeContent> youtubeContent = new ArrayList<>();
}
