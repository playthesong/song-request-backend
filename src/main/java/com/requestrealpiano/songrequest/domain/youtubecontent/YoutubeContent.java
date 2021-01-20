package com.requestrealpiano.songrequest.domain.youtubecontent;

import com.requestrealpiano.songrequest.domain.song.Song;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class YoutubeContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "youtube_content_id")
    private Long id;

    @Column(name = "channel_id")
    private Long channelId;

    private String title;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;
}
