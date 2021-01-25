package com.requestrealpiano.songrequest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.LastFmResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.inner.LastFmTrack;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbTrack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SongServiceTest {

    @InjectMocks
    SongService songService;

    @Mock
    SearchApiService searchApiService;

    @ParameterizedTest
    @CsvSource({"Artist, Title, 3"})
    @DisplayName("SearchApiService를 통해 받아온 ManiaDB 데이터 검색 결과를 반환 하는 테스트")
    void search_mania_db(String artist, String title, int totalCount) throws JsonProcessingException {
        // given
        List<ManiaDbTrack> mockTracks = createMockManiaDbTracks();
        ManiaDbResponse testResponse = ManiaDbResponse.builder()
                                                      .totalCount(totalCount)
                                                      .tracks(mockTracks)
                                                      .build();

        // when
        when(searchApiService.searchManiaDbResponse(artist, title)).thenReturn(testResponse);
        ManiaDbResponse maniaDbResponse = songService.searchManiaDb(artist, title);

        // then
        assertAll(
                () -> assertThat(maniaDbResponse.getTotalCount()).isEqualTo(testResponse.getTotalCount()),
                () -> assertThat(maniaDbResponse.getTracks()
                                                .stream()
                                                .filter(maniaDbTrack -> maniaDbTrack.getArtist().contains(artist))
                                                .count()).isEqualTo(totalCount)
        );

    }

    private List<ManiaDbTrack> createMockManiaDbTracks() {
        ManiaDbTrack firstTrack = ManiaDbTrack.builder()
                                              .artist("Artist 1")
                                              .title("Title 1")
                                              .imageUrl("http://imageUrl_1")
                                              .build();
        ManiaDbTrack secondTrack = ManiaDbTrack.builder()
                                               .artist("Artist 2")
                                               .title("Title 2")
                                               .imageUrl("http://imageUrl_2")
                                               .build();
        ManiaDbTrack thirdTrack = ManiaDbTrack.builder()
                                              .artist("Artist 3")
                                              .title("Title 3")
                                              .imageUrl("http://imageUrl_3")
                                              .build();
        return Arrays.asList(firstTrack, secondTrack, thirdTrack);
    }

    @Test
    @DisplayName("SearchApiService로부터 받아온 LastFM 검색 결과 데이터를 반환하는 테스트")
    void return_lastfm_response() throws JsonProcessingException {
        // given
        String notExistArtist = "Not Exist Artist";
        String notExistTitle = "Not Exist Title";

        List<LastFmTrack> tracks = new ArrayList<>();
        int totalCount = tracks.size();
        LastFmResponse mockLastFmResponse = LastFmResponse.of(totalCount, tracks);

        // when
        when(searchApiService.searchLastFmResponse(notExistArtist, notExistTitle)).thenReturn(mockLastFmResponse);
        LastFmResponse lastFmResponse = songService.searchLastFm(notExistArtist, notExistTitle);

        // then
        assertThat(lastFmResponse.getTotalCount()).isEqualTo(totalCount);
        assertThat(lastFmResponse.getTracks()).isEqualTo(tracks);
    }
}
