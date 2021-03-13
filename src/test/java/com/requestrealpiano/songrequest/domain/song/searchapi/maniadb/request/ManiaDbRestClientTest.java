package com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.request;

import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.ManiaDbRestClient;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(ManiaDbRestClient.class)
class ManiaDbRestClientTest {

    @Autowired
    ManiaDbRestClient maniaDbRestClient;

    @Autowired
    MockRestServiceServer server;

    @BeforeEach
    void setup() {
        server = MockRestServiceServer.bindTo(new RestTemplate())
                                      .build();
    }

    @ParameterizedTest
    @MethodSource("searchManiaDbXmlResponseParameters")
    @DisplayName("ManiaDb XML 결과 값 테스트")
    void search_maniadb_xml_response(Path xmlPath, String requestURI, String artist, String title) throws IOException {
        // given
        String testXmlResponse = Files.readString(xmlPath);

        // when
        server.expect(requestTo(requestURI))
              .andExpect(queryParam("artist", artist))
              .andExpect(queryParam("title", title))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess(testXmlResponse, MediaType.APPLICATION_XML));

        String xmlResponse = maniaDbRestClient.searchManiaDb(artist, title);

        // then
        assertThat(xmlResponse).contains(artist);
        assertThat(xmlResponse).contains(title);
    }

    private static Stream<Arguments> searchManiaDbXmlResponseParameters() {
        return Stream.of(
                Arguments.of(Path.of("src/test/resources/expectedresponse/maniadb/maniadb_response.xml"),
                             "http://api.maniadb.com", "김동률", "감사")
        );
    }
}
