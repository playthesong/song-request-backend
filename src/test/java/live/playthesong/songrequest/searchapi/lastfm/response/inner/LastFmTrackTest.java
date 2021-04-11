package live.playthesong.songrequest.searchapi.lastfm.response.inner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LastFmTrackTest {

    @ParameterizedTest
    @MethodSource("lastFmTrackParameters")
    @DisplayName("LastFm Track 데이터 직렬화 테스트")
    void serialize_lastfm_track(String title, String artist, String imageUrl,
                                String testJson) throws JsonProcessingException {
        // given
        LastFmTrack track = LastFmTrack.builder()
                                       .title(title)
                                       .artist(artist)
                                       .imageUrl(imageUrl)
                                       .build();
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        String json = objectMapper.writeValueAsString(track);

        // then
        assertThat(json).isEqualTo(testJson);
    }

    @ParameterizedTest
    @MethodSource("lastFmTrackParameters")
    @DisplayName("LastFm Track 데이터 역직렬화 테스트")
    void deserialize_lastfm_track(String title, String artist, String imageUrl,
                                  String testJson) throws JsonProcessingException {
        // given
        LastFmTrack track = LastFmTrack.builder()
                                       .title(title)
                                       .artist(artist)
                                       .imageUrl(imageUrl)
                                       .build();
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        LastFmTrack testTrack = objectMapper.readValue(testJson, LastFmTrack.class);

        // then
        assertAll(
                () -> assertThat(track.getArtist()).isEqualTo(testTrack.getArtist()),
                () -> assertThat(track.getTitle()).isEqualTo(testTrack.getTitle()),
                () -> assertThat(track.getImageUrl()).isEqualTo(testTrack.getImageUrl())
        );
    }

    private static Stream<Arguments> lastFmTrackParameters() {
        return Stream.of(
                Arguments.of("Title", "Artist", "http://imageUrl",
                             "{\"title\":\"Title\",\"artist\":\"Artist\",\"imageUrl\":\"http://imageUrl\"}")
        );
    }
}
