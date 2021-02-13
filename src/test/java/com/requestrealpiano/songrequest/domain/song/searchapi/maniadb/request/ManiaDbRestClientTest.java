package com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.request;

import com.requestrealpiano.songrequest.config.searchapi.ManiaDbProperties;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.ManiaDbRestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest(classes = {ManiaDbRestClient.class, ManiaDbRestClientTest.TestConfiguration.class})
class ManiaDbRestClientTest {

    @Autowired
    ManiaDbRestClient maniaDbRestClient;

    @EnableConfigurationProperties(ManiaDbProperties.class)
    public static class TestConfiguration { }

    @Test
    @DisplayName("검색 파라미터가 Null 또는 빈 문자열일 때 정상 실행 검증 테스트")
    void when_title_or_artist_null_does_not_throw_exception() {
        // given
        String emptyArtist = "";
        String emptyTitle = "";

        // then
        assertThatCode(() -> maniaDbRestClient.searchManiaDb(emptyArtist, emptyTitle)).doesNotThrowAnyException();
        assertThatCode(() -> maniaDbRestClient.searchManiaDb(null, null)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("검색 파라미터에 맞는 결과 값 검증 테스트")
    void validate_maniadb_xml_result() {
        // given
        String artist = "김동률";
        String title = "감사";

        // when
        String xml = maniaDbRestClient.searchManiaDb(artist, title);

        // then
        assertThat(xml).contains(artist, title);
    }
}
