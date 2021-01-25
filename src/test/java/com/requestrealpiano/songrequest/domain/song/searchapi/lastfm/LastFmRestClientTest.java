package com.requestrealpiano.songrequest.domain.song.searchapi.lastfm;

import com.requestrealpiano.songrequest.config.searchapi.LastFmProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

// 실제 API 요청 테스트를 하기 위해 @EnableConfigurationProperties(LastFmProperties.class) 설정 추가
@SpringBootTest(classes = {LastFmRestClient.class, LastFmRestClientTest.TestConfiguration.class})
class LastFmRestClientTest {

    @Autowired
    LastFmRestClient lastFmRestClient;

    @EnableConfigurationProperties(LastFmProperties.class)
    public static class TestConfiguration { }

    @Test
    @DisplayName("검색 파라미터가 Null 또는 빈 문자열일 때 정상 실행 검증 테스트")
    void test_when_track_or_artist_null() {
        // given
        String emptyArtist = "";
        String emptyTitle = "";

        // then
        assertThatCode(() -> lastFmRestClient.searchLastFm(emptyArtist, emptyTitle)).doesNotThrowAnyException();
        assertThatCode(() -> lastFmRestClient.searchLastFm(null, null)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("LastFM JSON 결과 값 테스트")
    void search_lastfm_json_response() {
        // given
        String artist = "김동률";
        String title = "감사";

        // when
        String json = lastFmRestClient.searchLastFm(artist, title);

        // then
        assertThat(json).contains(artist, title);
    }
}
