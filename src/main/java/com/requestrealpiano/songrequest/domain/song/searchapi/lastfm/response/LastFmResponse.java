package com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response;

import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.inner.LastFmTrack;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class LastFmResponse {

    private final int totalCount;
    private final List<LastFmTrack> tracks;

    @Builder
    private LastFmResponse(int totalCount, List<LastFmTrack> tracks) {
        this.totalCount = totalCount;
        this.tracks = tracks;
    }

    public static LastFmResponse of(int totalCount, List<LastFmTrack> tracks) {
        return LastFmResponse.builder()
                             .totalCount(totalCount)
                             .tracks(tracks)
                             .build();
    }
}
