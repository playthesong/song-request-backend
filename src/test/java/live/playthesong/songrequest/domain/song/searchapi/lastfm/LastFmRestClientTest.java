package live.playthesong.songrequest.domain.song.searchapi.lastfm;

import live.playthesong.songrequest.domain.song.searchapi.request.SearchSongParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static live.playthesong.songrequest.testobject.SongFactory.createSearchSongParametersOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@Disabled
@RestClientTest(LastFmRestClient.class)
class LastFmRestClientTest {

    @Autowired
    LastFmRestClient lastFmRestClient;

    @Autowired
    MockRestServiceServer server;

    @BeforeEach
    void setup() {
        server = MockRestServiceServer.bindTo(new RestTemplate())
                                      .build();
    }

    @ParameterizedTest
    @MethodSource("searchLastFmJsonResponseParameters")
    @DisplayName("LastFM JSON 결과 값 테스트")
    void search_lastfm_json_response(Path jsonPath, String requestURI, String artist, String title) throws IOException {
        // given
        SearchSongParameters parameters = createSearchSongParametersOf(artist, title);
        String testJsonResponse = Files.readString(jsonPath);

        // when
        server.expect(requestTo(requestURI))
              .andExpect(queryParam("artist", artist))
              .andExpect(queryParam("title", title))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess(testJsonResponse, MediaType.APPLICATION_JSON));

        String jsonResponse = lastFmRestClient.searchLastFm(parameters);

        // then
        assertThat(jsonResponse).contains(artist);
        assertThat(jsonResponse).contains(title);
    }

    private static Stream<Arguments> searchLastFmJsonResponseParameters() {
        return Stream.of(
                Arguments.of(Path.of("src/test/resources/expectedresponse/lastfm/lastfm_response.json"),
                             "https://www.last.fm", "김동률", "감사")
        );
    }
}
