package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.LastFmRestClient;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.inner.Track;
import com.requestrealpiano.songrequest.domain.song.searchapi.translator.JsonTranslator;
import com.requestrealpiano.songrequest.service.searchapi.LastFmApiService;
import com.requestrealpiano.songrequest.service.searchapi.SearchApiService;
import com.requestrealpiano.songrequest.testconfig.searchapi.LastFm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.mock.mockito.SpyBeans;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(MockitoExtension.class)
public class LastFmApiServiceTest {

    @InjectMocks
    LastFmApiService lastFmApiService;

    @Mock
    LastFmRestClient lastFmRestClient;

    @Spy
    JsonTranslator jsonTranslator;

    @ParameterizedTest
    @MethodSource("searchLastFmResponseParameters")
    @DisplayName("LastFM 검색 결과 반환 테스트")
    void search_lastfm_response(Path jsonPath, String artist, String title, int totalCount) throws IOException {
        // given
        String testJsonResponse = Files.readString(jsonPath);

        // when
        when(lastFmRestClient.searchLastFm(artist, title)).thenReturn(testJsonResponse);
        SearchApiResponse response = lastFmApiService.requestSearchApiResponse(artist, title);
        List<Track> tracks = response.getTracks();

        // then
        assertAll(
                () -> assertThat(response.getTotalCount()).isEqualTo(totalCount),
                () -> assertThat(tracks.stream().allMatch(track -> track.getArtist().contains(artist))).isTrue(),
                () -> assertThat(tracks.stream().allMatch(track -> track.getTitle().contains(title))).isTrue()
        );
    }

    private static Stream<Arguments> searchLastFmResponseParameters() {
        return Stream.of(
                Arguments.of(Path.of("src/test/resources/expectedresponse/lastfm/lastfm_response.json"),
                             "김동률", "감사", 10)
        );
    }
}
