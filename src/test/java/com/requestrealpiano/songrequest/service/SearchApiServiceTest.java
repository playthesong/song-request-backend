package com.requestrealpiano.songrequest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.config.searchapi.LastFmProperties;
import com.requestrealpiano.songrequest.config.searchapi.ManiaDbProperties;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.LastFmRestClient;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.ManiaDbRestClient;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.inner.Track;
import com.requestrealpiano.songrequest.domain.song.searchapi.translator.JsonTranslator;
import com.requestrealpiano.songrequest.domain.song.searchapi.translator.XmlTranslator;
import com.requestrealpiano.songrequest.service.searchapi.LastFmApiService;
import com.requestrealpiano.songrequest.service.searchapi.ManiaDbApiService;
import com.requestrealpiano.songrequest.service.searchapi.SearchApiService;
import com.requestrealpiano.songrequest.testconfig.annotation.LastFm;
import com.requestrealpiano.songrequest.testconfig.annotation.ManiaDb;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = {ManiaDbApiService.class, ManiaDbRestClient.class, LastFmApiService.class, LastFmRestClient.class,
                           JsonTranslator.class, XmlTranslator.class, SearchApiServiceTest.TestConfiguration.class})
class SearchApiServiceTest {

    @Autowired
    @ManiaDb
    SearchApiService maniaDbApiService;

    @Autowired
    @LastFm
    SearchApiService lastFmApiService;

    @Autowired
    ManiaDbRestClient maniaDbRestClient;

    @Autowired
    LastFmRestClient lastFmRestClient;

    @Autowired
    JsonTranslator jsonTranslator;

    @Autowired
    XmlTranslator xmlTranslator;

    @EnableConfigurationProperties({ManiaDbProperties.class, LastFmProperties.class})
    public static class TestConfiguration { }

    @ParameterizedTest
    @MethodSource("searchResponseParameters")
    @DisplayName("ManiaDB 검색 결과 반환 테스트")
    void search_maniaDb_response(String artist, String title, int totalCount) throws IOException {
        // when
        SearchApiResponse maniaDbResponse = maniaDbApiService.requestSearchApiResponse(artist, title);
        List<Track> tracks = maniaDbResponse.getTracks();

        /* 0 - 첫 번째 검색 결과 인덱스 */
        Track track = tracks.get(0);

        // then
        assertAll(
                () -> assertThat(maniaDbResponse.getTotalCount()).isEqualTo(totalCount),
                () -> assertThat(track.getArtist()).contains(artist),
                () -> assertThat(track.getTitle()).contains(title)
        );
    }

    @ParameterizedTest
    @MethodSource("searchResponseParameters")
    @DisplayName("LastFM 검색 결과 반환 테스트")
    void search_lastfm_response(String artist, String title, int totalCount) throws JsonProcessingException {
        // when
        SearchApiResponse lastFmResponse = lastFmApiService.requestSearchApiResponse(artist, title);
        List<Track> tracks = lastFmResponse.getTracks();

        // then
        assertAll(
                () -> assertThat(lastFmResponse.getTotalCount()).isEqualTo(totalCount),
                () -> assertThat(tracks.stream()
                                       .filter(track -> track.getArtist().contains(artist))
                                       .count()).isEqualTo(totalCount),
                () -> assertThat(tracks.stream()
                                       .filter(track -> track.getTitle().contains(title))
                                       .count()).isEqualTo(totalCount)
        );
    }

    private static Stream<Arguments> searchResponseParameters() {
        return Stream.of(
                Arguments.of("김동률", "감사", 10)
        );
    }
}
