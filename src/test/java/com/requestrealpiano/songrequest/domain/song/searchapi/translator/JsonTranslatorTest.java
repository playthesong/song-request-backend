package com.requestrealpiano.songrequest.domain.song.searchapi.translator;

import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.LastFmResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.inner.LastFmTrack;
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
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class JsonTranslatorTest {

    @InjectMocks
    JsonTranslator jsonTranslator;

    @ParameterizedTest
    @MethodSource("mapJsonToLastFmResponseParameters")
    @DisplayName("LastFM JSON 응답으로부터 DTO를 생성하는 테스트")
    void map_json_to_lastfm_response(int totalCount, String artist, String title, String imageUrl) throws IOException {
        // given
        Path jsonFilePath = Path.of("src/test/resources/expectedresponse/lastfm/lastfm_response.json");
        String testJson = Files.readString(jsonFilePath);

        // when
        LastFmResponse lastFmResponse = jsonTranslator.mapToLastFmResponse(testJson);
        List<LastFmTrack> tracks = lastFmResponse.getTracks();

        /* 0 - 첫 LastFm Track 데이터의 인덱스 */
        LastFmTrack track = tracks.get(0);

        // then
        assertAll(
                () -> assertThat(tracks.size()).isEqualTo(totalCount),
                () -> assertThat(track.getArtist()).isEqualTo(artist),
                () -> assertThat(track.getTitle()).isEqualTo(title),
                () -> assertThat(track.getImageUrl()).isEqualTo(imageUrl)
        );
    }

    private static Stream<Arguments> mapJsonToLastFmResponseParameters() {
        return Stream.of(
                Arguments.of(10, "김동률", "감사",
                             "https://www.last.fm/music/%EA%B9%80%EB%8F%99%EB%A5%A0/_/%EA%B0%90%EC%82%AC")
        );
    }
}
