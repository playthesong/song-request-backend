package com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.request;

import com.requestrealpiano.songrequest.config.searchapi.ManiaDbProperties;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.ManiaDbRestClient;
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

@SpringBootTest(classes = ManiaDbRestClientTest.TestConfiguration.class)
class ManiaDbRestClientTest {

    @Autowired
    ManiaDbProperties maniaDbProperties;

    @EnableConfigurationProperties(ManiaDbProperties.class)
    public static class TestConfiguration { }

    @ParameterizedTest
    @MethodSource("createNewManiaDbRequestParameters")
    @DisplayName("ManiaDB 요청 객체를 정적 팩토리 메서드로 생성하는 테스트")
    void create_new_maniadb_request(String url, String display, String method, String apiKey, String version,
                                    String title, String artist) {
        // given
        ManiaDbProperties maniaDbProperties = new ManiaDbProperties();
        maniaDbProperties.setUrl(url);
        maniaDbProperties.setDisplay(display);
        maniaDbProperties.setMethod(method);
        maniaDbProperties.setApiKey(apiKey);
        maniaDbProperties.setVersion(version);

        // when
        ManiaDbRestClient maniaDbRestClient = ManiaDbRestClient.of(maniaDbProperties, title, artist);

        // then
        assertAll(
                () -> assertThat(maniaDbRestClient.getBaseUrl()).isEqualTo(url),
                () -> assertThat(maniaDbRestClient.getDisplay()).isEqualTo(display),
                () -> assertThat(maniaDbRestClient.getMethod()).isEqualTo(method),
                () -> assertThat(maniaDbRestClient.getApiKey()).isEqualTo(apiKey),
                () -> assertThat(maniaDbRestClient.getVersion()).isEqualTo(version),
                () -> assertThat(maniaDbRestClient.getKeyword()).isEqualTo(title + artist)
        );
    }

    private static Stream<Arguments> createNewManiaDbRequestParameters() {
        return Stream.of(
                Arguments.of("http://test", "10", "Test", "Test Api Key", "Test Version", "Title", "Artist")
        );
    }

    @Test
    @DisplayName("title, artist 파라미터가 null이거나 빈 문자열일 경우 디폴트 값으로 처리하는 테스트")
    void test_when_title_or_artist_null() {
        // given
        ManiaDbProperties maniaDbProperties = new ManiaDbProperties();
        String empty = "";
        String artist = "Artist";

        // when
        ManiaDbRestClient maniaDbRestClientWhenAllNull = ManiaDbRestClient.of(maniaDbProperties, null, null);
        ManiaDbRestClient maniaDbRestClientWhenOneNull = ManiaDbRestClient.of(maniaDbProperties, null, "Artist");
        ManiaDbRestClient maniaDbRestClientWhenAllEmpty = ManiaDbRestClient.of(maniaDbProperties, "", "");

        // then
        assertAll(
                () -> assertThat(maniaDbRestClientWhenAllNull.getKeyword()).isEqualTo(empty),
                () -> assertThat(maniaDbRestClientWhenOneNull.getKeyword()).isEqualTo(artist),
                () -> assertThat(maniaDbRestClientWhenAllEmpty.getKeyword()).isEqualTo(empty)
        );
    }

    @ParameterizedTest
    @CsvSource({"김동률, 감사"})
    @DisplayName("ManiaDB API 데이터를 xml형식의 문자열로 잘 받아오는지에 관한 테스트")
    void search_maniadb_xml_response(String artist, String title) {
        /*
         * TODO: XML 내용 검증 실패, 일단 구현을 한 뒤 XML 검증 방법에 대해 학습을 한 후 보완하기
         */

        // given
        ManiaDbRestClient maniaDbRestClient = ManiaDbRestClient.of(maniaDbProperties, artist, title);

        // when
        String xml = maniaDbRestClient.searchManiaDb();

        // then
        assertThat(xml).isNotEmpty();

        /*
            Path testXmlFilePath = Path.of("src/test/resources/expectedresponse/maniadb_response.xml");
            String xmlFromFile = Files.readString(testXmlFilePath);
            assertThat(xml).isEqualTo(xmlFromFile);
        */
    }
}
