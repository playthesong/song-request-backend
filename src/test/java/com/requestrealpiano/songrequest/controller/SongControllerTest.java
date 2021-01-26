package com.requestrealpiano.songrequest.controller;

import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.LastFmResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.inner.LastFmTrack;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbTrack;
import com.requestrealpiano.songrequest.service.SongService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SongController.class)
class SongControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SongService songService;

    @Test
    @DisplayName("ManiaDB 검색 결과 반환 API 테스트")
    void search_maniadb() throws Exception {
        // given
        String artist = "Artist";
        String title = "title";

        ManiaDbResponse maniaDbResponse = createMockManiaDbResponse();
        int totalCount = maniaDbResponse.getTotalCount();
        List<ManiaDbTrack> tracks = maniaDbResponse.getTracks();

        /* 0 - Tracks의 첫 번째 트랙 인덱스 */
        ManiaDbTrack track = tracks.get(0);

        // when
        when(songService.searchManiaDb(artist, title)).thenReturn(maniaDbResponse);
        ResultActions result = mockMvc.perform(get("/songs/search-api/k-pop")
                                                .param("artist", artist)
                                                .param("title", title)
                                                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
              .andExpect(status().isOk())
              .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
              .andExpect(jsonPath("success").value(true))
              .andExpect(jsonPath("statusMessage").value("OK"))
              .andExpect(jsonPath("data.totalCount").value(totalCount))
              .andExpect(jsonPath("data.tracks.length()").value(tracks.size()))
              .andExpect(jsonPath("data.tracks[0].title").value(track.getTitle()))
              .andExpect(jsonPath("data.tracks[0].artist").value(track.getArtist()))
              .andExpect(jsonPath("data.tracks[0].imageUrl").value(track.getImageUrl()))
        ;
    }

    private ManiaDbResponse createMockManiaDbResponse() {
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
        List<ManiaDbTrack> tracks = Arrays.asList(firstTrack, secondTrack, thirdTrack);
        return ManiaDbResponse.builder()
                              .totalCount(tracks.size())
                              .tracks(tracks)
                              .build();
    }

    @ParameterizedTest
    @CsvSource("Artist Name, Song Title, http://imageUrl")
    @DisplayName("LastFM 검색 결과 반환 API 테스트")
    void search_lastfm_api(String artist, String title, String imageUrl) throws Exception {
        // given
        LastFmTrack track = LastFmTrack.builder()
                                       .artist(artist)
                                       .title(title)
                                       .imageUrl(imageUrl)
                                       .build();
        List<LastFmTrack> tracks = Collections.singletonList(track);
        int totalCount = tracks.size();
        LastFmResponse lastFmResponse = LastFmResponse.of(totalCount, tracks);

        // when
        when(songService.searchLastFm(artist, title)).thenReturn(lastFmResponse);

        ResultActions result = mockMvc.perform(get("/songs/search-api/pop")
                                        .param("artist", artist)
                                        .param("title", title)
                                        .accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
              .andExpect(status().isOk())
              .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
              .andExpect(jsonPath("success").value(true))
              .andExpect(jsonPath("statusMessage").value("OK"))
              .andExpect(jsonPath("data.totalCount").value(totalCount))
              .andExpect(jsonPath("data.tracks.length()").value(totalCount))
              .andExpect(jsonPath("data.tracks[0].title").value(track.getTitle()))
              .andExpect(jsonPath("data.tracks[0].artist").value(track.getArtist()))
              .andExpect(jsonPath("data.tracks[0].imageUrl").value(track.getImageUrl()))
        ;
    }

}
