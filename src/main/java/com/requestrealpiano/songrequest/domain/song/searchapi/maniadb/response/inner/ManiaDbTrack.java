package com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ManiaDbTrack {

    private final String title;
    private final String artist;
    private final String imageUrl;

    @Builder
    private ManiaDbTrack(String title, String artist, String imageUrl) {
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
    }

    public static ManiaDbTrack from(ManiaDbTrackData trackData) {
        ManiaDbArtistData artistData = trackData.getArtistData();
        ManiaDbAlbumData albumData = trackData.getAlbumData();

        return ManiaDbTrack.builder()
                           .title(trackData.getTitle())
                           .artist(artistData.getName())
                           .imageUrl(albumData.getImageUrl())
                           .build();
    }
}
