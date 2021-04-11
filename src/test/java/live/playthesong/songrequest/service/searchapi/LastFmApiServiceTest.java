package live.playthesong.songrequest.service.searchapi;

import live.playthesong.songrequest.searchapi.lastfm.LastFmRestClient;
import live.playthesong.songrequest.searchapi.request.SearchSongParameters;
import live.playthesong.songrequest.searchapi.response.SearchApiResponse;
import live.playthesong.songrequest.searchapi.response.inner.Track;
import live.playthesong.songrequest.searchapi.translator.JsonTranslator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static live.playthesong.songrequest.testobject.SongFactory.createSearchSongParametersOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
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
        SearchSongParameters parameters = createSearchSongParametersOf(artist, title);
        String testJsonResponse = Files.readString(jsonPath);

        // when
        when(lastFmRestClient.searchLastFm(parameters)).thenReturn(testJsonResponse);
        SearchApiResponse response = lastFmApiService.requestSearchApiResponse(parameters);
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
