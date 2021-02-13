package com.requestrealpiano.songrequest.domain.song.searchapi.response;

import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.inner.LastFmTrack;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbClientResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbData;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbTrackData;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.inner.Track;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
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

        SearchApiResponse.SearchApiResponseBuilder builder = SearchApiResponse.builder();
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
        return SearchApiResponse.builder()
                                .totalCount(tracks.size())
                                .tracks(tracks.stream().map(Track::from).collect(Collectors.toList()))
                                .build();
    }
}
