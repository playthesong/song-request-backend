package com.requestrealpiano.songrequest.domain.song.searchapi.lastfm;

import com.requestrealpiano.songrequest.config.searchapi.LastFmProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = LastFmRestClientTest.TestConfiguration.class)
class LastFmRestClientTest {

    @Autowired
    LastFmProperties lastFmProperties;

    @ParameterizedTest
    @MethodSource("createNewLastFmRestClientParameters")
    @DisplayName("LastFM 요청 클라이언트 객체를 정적 팩토리 메서드로 생성하는 테스트")
    void create_new_lastfm_rest_client(String url, String method, String limit, String apiKey, String format,
                                       String track, String artist) {
        // given
        LastFmProperties lastFmProperties = new LastFmProperties();
        lastFmProperties.setUrl(url);
        lastFmProperties.setMethod(method);
        lastFmProperties.setLimit(limit);
        lastFmProperties.setApiKey(apiKey);
        lastFmProperties.setFormat(format);

        // when
        LastFmRestClient lastFmRestClient = LastFmRestClient.of(lastFmProperties, artist, track);

        // then
        assertAll(
                () -> assertThat(lastFmRestClient.getBaseUrl()).isEqualTo(url),
                () -> assertThat(lastFmRestClient.getMethod()).isEqualTo(method),
                () -> assertThat(lastFmRestClient.getLimit()).isEqualTo(limit),
                () -> assertThat(lastFmRestClient.getApiKey()).isEqualTo(apiKey),
                () -> assertThat(lastFmRestClient.getFormat()).isEqualTo(format),
                () -> assertThat(lastFmRestClient.getArtist()).isEqualTo(artist),
                () -> assertThat(lastFmRestClient.getTrack()).isEqualTo(track)
        );
    }

    private static Stream<Arguments> createNewLastFmRestClientParameters() {
        return Stream.of(
                Arguments.of("http://testUrl", "track.search", "10", "Test Api Key", "json", "Title", "Artist")
        );
    }

    @Test
    @DisplayName("track, artist 파라미터가 null이거나 빈 문자열일 경우 디폴트 값으로 처리하는 테스트")
    void test_when_track_or_artist_null() {
        // given
        LastFmProperties lastFmProperties = new LastFmProperties();
        String empty = "";
        String track = "Track";

        // when
        LastFmRestClient lastFmRestClientWhenAllNull = LastFmRestClient.of(lastFmProperties, null, null);
        LastFmRestClient lastFmRestClientWhenArtistIsNull = LastFmRestClient.of(lastFmProperties, null, track);
        LastFmRestClient lastFmRestClientWhenAllEmpty = LastFmRestClient.of(lastFmProperties, "", "");

        // then
        assertAll(
                () -> assertThat(lastFmRestClientWhenAllNull.getArtist()).isEqualTo(empty),
                () -> assertThat(lastFmRestClientWhenAllNull.getTrack()).isEqualTo(empty),
                () -> assertThat(lastFmRestClientWhenArtistIsNull.getArtist()).isEqualTo(empty),
                () -> assertThat(lastFmRestClientWhenAllEmpty.getTrack()).isEqualTo(empty),
                () -> assertThat(lastFmRestClientWhenAllEmpty.getArtist()).isEqualTo(empty)
        );
    }

    /* 실제 API 요청 테스트를 하기 위해 다음의 설정 추가 */
    @EnableConfigurationProperties(LastFmProperties.class)
    public static class TestConfiguration { }

    @ParameterizedTest
    @CsvSource({"김동률, 감사"})
    @DisplayName("LastFM JSON 결과 값 테스트")
    void search_lastfm_json_response(String artist, String track) {
        // given
        LastFmRestClient lastFmRestClient = LastFmRestClient.of(lastFmProperties, artist, track);

        // when
        String jsonResponse = lastFmRestClient.searchLastFm();

        // then
        assertThat(jsonResponse).contains(artist, track);
    }
}
