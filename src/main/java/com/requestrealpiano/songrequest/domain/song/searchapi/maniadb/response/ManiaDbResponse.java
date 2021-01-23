package com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response;

import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbData;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbTrack;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbTrackData;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ManiaDbResponse {

    private final int totalCount;
    private final List<ManiaDbTrack> tracks;

    @Builder
    private ManiaDbResponse(int totalCount, List<ManiaDbTrack> tracks) {
        this.totalCount = totalCount;
        this.tracks = tracks;
    }

    public static ManiaDbResponse from(ManiaDbClientResponse maniaDbClientResponse) {
        ManiaDbData maniaDbData = maniaDbClientResponse.getManiaDbData();
        List<ManiaDbTrackData> tracks = maniaDbData.getTracks();

        return ManiaDbResponse.builder()
                .totalCount(maniaDbData.getTotalCount())
                .tracks(tracks.stream()
                              .map(ManiaDbTrack::from)
                              .collect(Collectors.toList()))
                .build();
    }
}
