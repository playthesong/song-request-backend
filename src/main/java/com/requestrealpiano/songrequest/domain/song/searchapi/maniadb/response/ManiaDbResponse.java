package com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response;

import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbData;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbTrack;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbTrackData;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
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
        int totalCount = maniaDbData.getTotalCount();

        ManiaDbResponseBuilder builder = ManiaDbResponse.builder();
        builder.totalCount(totalCount)
               .build();

        // 0 - 검색 결과 개수
        if (totalCount == 0) {
            return builder.tracks(Collections.emptyList())
                          .build();
        }
           return builder.tracks(tracks.stream().map(ManiaDbTrack::from).collect(Collectors.toList()))
                         .build();
    }
}
