package com.requestrealpiano.songrequest.domain.letter.dto.response.inner;

import com.requestrealpiano.songrequest.domain.song.Song;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SongSummary {

    private final Long id;
    private final String title;
    private final String artist;
    private final String imageUrl;

    @Builder
    private SongSummary(Long id, String title, String artist, String imageUrl) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
    }

    public static SongSummary from(Song song) {
        return SongSummary.builder()
                          .id(song.getId())
                          .title(song.getSongTitle())
                          .artist(song.getArtist())
                          .imageUrl(song.getImageUrl())
                          .build();
    }
}
