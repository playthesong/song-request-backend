package live.playthesong.songrequest.searchapi.response.inner;

import live.playthesong.songrequest.searchapi.lastfm.response.inner.LastFmTrack;
import live.playthesong.songrequest.searchapi.maniadb.response.inner.ManiaDbAlbumData;
import live.playthesong.songrequest.searchapi.maniadb.response.inner.ManiaDbArtistData;
import live.playthesong.songrequest.searchapi.maniadb.response.inner.ManiaDbTrackData;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Track {

    private final String title;
    private final String artist;
    private final String imageUrl;

    @Builder
    private Track(String title, String artist, String imageUrl) {
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
    }

    public static Track from(ManiaDbTrackData trackData) {
        ManiaDbArtistData artistData = trackData.getArtistData();
        ManiaDbAlbumData albumData = trackData.getAlbumData();

        return Track.builder()
                    .title(trackData.getTitle())
                    .artist(artistData.getName())
                    .imageUrl(albumData.getImageUrl())
                    .build();
    }

    public static Track from(LastFmTrack lastFmTrack) {
        return Track.builder()
                    .title(lastFmTrack.getTitle())
                    .artist(lastFmTrack.getArtist())
                    .imageUrl(lastFmTrack.getImageUrl())
                    .build();
    }
}
