package live.playthesong.songrequest.domain.song.searchapi.translator;

import live.playthesong.songrequest.domain.song.searchapi.response.SearchApiResponse;
import live.playthesong.songrequest.domain.song.searchapi.response.inner.Track;
import live.playthesong.songrequest.global.error.exception.ParsingFailedException;
import live.playthesong.songrequest.global.error.exception.parsing.SearchResultParsingException;
import live.playthesong.songrequest.global.error.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class JsonTranslatorTest {

    @InjectMocks
    JsonTranslator jsonTranslator;

    @ParameterizedTest
    @MethodSource("mapJsonToLastFmResponseParameters")
    @DisplayName("LastFM JSON 응답으로부터 DTO를 생성하는 테스트")
    void map_json_to_lastfm_response(Path testJsonPath, int first, int totalCount, String artist,
                                     String title, String imageUrl) throws IOException {
        // given
        String testJson = Files.readString(testJsonPath);

        // when
        SearchApiResponse lastFmResponse = jsonTranslator.mapToLastFmResponse(testJson);
        List<Track> tracks = lastFmResponse.getTracks();

        Track track = tracks.get(first);

        // then
        assertAll(
                () -> assertThat(tracks.size()).isEqualTo(totalCount),
                () -> assertThat(track.getArtist()).isEqualTo(artist),
                () -> assertThat(track.getTitle()).isEqualTo(title),
                () -> assertThat(track.getImageUrl()).isEqualTo(imageUrl)
        );
    }

    @ParameterizedTest
    @MethodSource("jsonParsingFailedParameters")
    @DisplayName("ERROR - ParsingFailedException 테스트")
    void json_parsing_failed(String wrongLastFmResult) {
        // given
        String errorMessage = ErrorCode.SEARCH_RESULT_ERROR.getMessage();

        // then
        assertThatThrownBy(() -> jsonTranslator.mapToLastFmResponse(wrongLastFmResult))
                .isExactlyInstanceOf(SearchResultParsingException.class)
                .isInstanceOf(ParsingFailedException.class)
                .hasMessage(errorMessage);

    }

    private static Stream<Arguments> mapJsonToLastFmResponseParameters() {
        return Stream.of(
                Arguments.of(Path.of("src/test/resources/expectedresponse/lastfm/lastfm_response.json"),
                                     0, 10, "김동률", "감사",
                                     "https://www.last.fm/music/%EA%B9%80%EB%8F%99%EB%A5%A0/_/%EA%B0%90%EC%82%AC")
        );
    }

    private static Stream<Arguments> jsonParsingFailedParameters() {
        return Stream.of(
                Arguments.of("Something wrong result")
        );
    }
}
