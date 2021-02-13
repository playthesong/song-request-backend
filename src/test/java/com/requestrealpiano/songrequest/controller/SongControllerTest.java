package com.requestrealpiano.songrequest.controller;

import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.inner.LastFmTrack;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.inner.Track;
import com.requestrealpiano.songrequest.service.SongService;
import com.requestrealpiano.songrequest.service.searchapi.SearchApiService;
import com.requestrealpiano.songrequest.testconfig.annotation.LastFm;
import com.requestrealpiano.songrequest.testconfig.annotation.ManiaDb;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("searchManiaDbParameters")
    @DisplayName("ManiaDB 검색 결과 반환 API 테스트")
    void search_mania_db(String artist, String title, int first) throws Exception {
        // given
        SearchApiResponse maniaDbResponse = createMockManiaDbResponse();
        int totalCount = maniaDbResponse.getTotalCount();
        List<Track> tracks = maniaDbResponse.getTracks();

        Track track = tracks.get(first);

        // when
        when(songService.searchSong(artist, title)).thenReturn(maniaDbResponse);
        ResultActions result = mockMvc.perform(get("/songs")
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

    private static Stream<Arguments> searchManiaDbParameters() {
        return Stream.of(
                Arguments.of("Artist", "title", 0)
        );
    }

    private SearchApiResponse createMockManiaDbResponse() {
        Track firstTrack = Track.builder()
                                 .artist("Artist 1")
                                 .title("Title 1")
                                 .imageUrl("http://imageUrl_1")
                                 .build();
        Track secondTrack = Track.builder()
                                 .artist("Artist 2")
                                 .title("Title 2")
                                 .imageUrl("http://imageUrl_2")
                                 .build();
        Track thirdTrack = Track.builder()
                                 .artist("Artist 3")
                                 .title("Title 3")
                                 .imageUrl("http://imageUrl_3")
                                 .build();
        List<Track> tracks = Arrays.asList(firstTrack, secondTrack, thirdTrack);
        return SearchApiResponse.builder()
                              .totalCount(tracks.size())
                              .tracks(tracks)
                              .build();
    }

    @ParameterizedTest
    @MethodSource("searchLastFmApiParameters")
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
        SearchApiResponse response = SearchApiResponse.from(tracks);

        // when
        when(songService.searchSong(artist, title)).thenReturn(response);

        ResultActions result = mockMvc.perform(get("/songs")
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

    private static Stream<Arguments> searchLastFmApiParameters() {
        return Stream.of(
                Arguments.of("Artist Name", "Song Title", "http://imageUrl")
        );
    }
}
