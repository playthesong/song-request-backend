package live.playthesong.songrequest.searchapi.response;

import live.playthesong.songrequest.searchapi.lastfm.response.inner.LastFmTrack;
import live.playthesong.songrequest.searchapi.maniadb.response.ManiaDbClientResponse;
import live.playthesong.songrequest.searchapi.maniadb.response.inner.ManiaDbData;
import live.playthesong.songrequest.searchapi.maniadb.response.inner.ManiaDbTrackData;
import live.playthesong.songrequest.searchapi.response.inner.Track;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class SearchApiResponse {

    private final int totalCount;
    private final List<Track> tracks;

    @Builder
    private SearchApiResponse(int totalCount, List<Track> tracks) {
        this.totalCount = totalCount;
        this.tracks = tracks;
    }

    public static SearchApiResponse from(ManiaDbClientResponse maniaDbClientResponse) {
        ManiaDbData maniaDbData = maniaDbClientResponse.getManiaDbData();
        List<ManiaDbTrackData> tracks = maniaDbData.getTracks();
        int totalCount = maniaDbData.getTotalCount();

        SearchApiResponseBuilder builder = SearchApiResponse.builder();
        builder.totalCount(totalCount)
               .build();

        if (totalCount == 0) {
            return builder.tracks(Collections.emptyList())
                          .build();
        }
        return builder.tracks(tracks.stream().map(Track::from).collect(Collectors.toList()))
                      .build();
    }

    public static SearchApiResponse from(List<LastFmTrack> tracks) {
        List<LastFmTrack> lastFmTracks = Optional.ofNullable(tracks).orElse(Collections.emptyList());
        int totalCount = lastFmTracks.size();

        SearchApiResponseBuilder builder = SearchApiResponse.builder();
        builder.totalCount(totalCount)
               .build();

        if (totalCount == 0) {
            return builder.tracks(Collections.emptyList())
                          .build();
        }

        return builder.tracks(lastFmTracks.stream().map(Track::from).collect(Collectors.toList()))
                      .build();
    }
}
