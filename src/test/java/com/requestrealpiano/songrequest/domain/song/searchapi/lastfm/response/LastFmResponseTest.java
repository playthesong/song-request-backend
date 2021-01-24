package com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response;

import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.inner.LastFmTrack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class LastFmResponseTest {

    @Test
    @DisplayName("정적 팩토리 메서드 of() 로부터 LastFm 검색 결과 DTO를 생성하는 테스트")
    void create_new_lastfm_response() {
        // given
        List<LastFmTrack> tracks = new ArrayList<>();
        int totalCount = tracks.size();

        // when
        LastFmResponse lastFmResponse = LastFmResponse.of(totalCount, tracks);

        // then
        assertThat(lastFmResponse.getTotalCount()).isEqualTo(totalCount);
        assertThat(lastFmResponse.getTracks()).isEqualTo(tracks);
    }
}
