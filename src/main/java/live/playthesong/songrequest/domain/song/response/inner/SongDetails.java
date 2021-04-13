package live.playthesong.songrequest.domain.song.response.inner;

import live.playthesong.songrequest.domain.song.Song;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SongDetails {

    private final String title;
    private final String artist;
    private final String imageUrl;
    private final int requestCount;

    @Builder
    private SongDetails(String title, String artist, String imageUrl, int requestCount) {
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.requestCount = requestCount;
    }

    public static SongDetails from(Song song) {
        return SongDetails.builder()
                          .title(song.getSongTitle())
                          .artist(song.getArtist())
                          .imageUrl(song.getImageUrl())
                          .requestCount(song.getRequestCount())
                          .build();
    }
}
