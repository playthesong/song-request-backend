package com.requestrealpiano.songrequest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.config.searchapi.LastFmProperties;
import com.requestrealpiano.songrequest.config.searchapi.ManiaDbProperties;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.LastFmRestClient;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.LastFmResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.inner.LastFmTrack;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbTrack;
import com.requestrealpiano.songrequest.domain.song.searchapi.translator.JsonTranslator;
import com.requestrealpiano.songrequest.domain.song.searchapi.translator.XmlTranslator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = {SearchApiService.class, JsonTranslator.class, XmlTranslator.class, SearchApiServiceTest.TestConfiguration.class})
class SearchApiServiceTest {

    @Autowired
    SearchApiService searchApiService;

    @Autowired
    ManiaDbProperties maniaDbProperties;

    @Autowired
    LastFmProperties lastFmProperties;

    @Autowired
    JsonTranslator jsonTranslator;

    @Autowired
    XmlTranslator xmlTranslator;

    @EnableConfigurationProperties({ManiaDbProperties.class, LastFmProperties.class})
    public static class TestConfiguration { }

    @ParameterizedTest
    @CsvSource("김동률, 감사, http://i.maniadb.com/images/album/148/148138_1_f.jpg, " +
               "10")
    @DisplayName("ManiaDB 검색 결과 반환 테스트")
    void search_maniaDb_response(String artist, String title, String imageUrl, int totalCount) throws IOException {
        // given
        ManiaDbTrack testTrack = ManiaDbTrack.builder()
                                             .artist(artist)
                                             .title(title)
                                             .imageUrl(imageUrl)
                                             .build();

        /*
          TODO:
            각 의존성들을 Mocking 한 뒤 처리하려 했으나 정적 팩토리 메서드로 생성하는 과정을
            Mocktito.when().thenReturn() 으로 처리하면 아래와 같은 에러 발생하였다.
            우선 필요한 실제 빈을 주입하여 테스트하기로 하였고, 추후 기능 구현이 끝난 뒤 개선 방법이 없을 지 다시 고민 해보려 한다.

            org.mockito.exceptions.misusing.WrongTypeOfReturnValue:
            ManiaDbRestClient$MockitoMock$1303731471$MockitoMock$830141523 cannot be returned by getVersion()
            getVersion() should return String

            ***
            If you're unsure why you're getting above error read on.
            Due to the nature of the syntax above problem might occur because:
            1. This exception *might* occur in wrongly written multi-threaded tests.
               Please refer to Mockito FAQ on limitations of concurrency testing.
            2. A spy is stubbed using when(spy.foo()).then() syntax. It is safer to stub spies -
               - with doReturn|Throw() family of methods. More in javadocs for Mockito.spy() method.
         */

        // when
        ManiaDbResponse maniaDbResponse = searchApiService.searchManiaDbResponse(artist, title);
        List<ManiaDbTrack> tracks = maniaDbResponse.getTracks();

        /* 0 - 첫 번째 검색 결과 인덱스 */
        ManiaDbTrack track = tracks.get(0);

        // then
        assertAll(
                () -> assertThat(maniaDbResponse.getTotalCount()).isEqualTo(totalCount),
                () -> assertThat(track.getTitle()).isEqualTo(testTrack.getTitle()),
                () -> assertThat(track.getArtist()).isEqualTo(testTrack.getArtist()),
                () -> assertThat(track.getImageUrl()).isEqualTo(testTrack.getImageUrl())
        );
    }

    @ParameterizedTest
    @MethodSource("searchLastFmResponseParameters")
    @DisplayName("LastFM 검색 결과 반환 테스트")
    void search_lastfm_response(String artist, String title, int totalCount) throws JsonProcessingException {
        // given
        LastFmRestClient lastFmRestClient = LastFmRestClient.of(lastFmProperties, artist, title);

        // when
        String rawJson = lastFmRestClient.searchLastFm();
        LastFmResponse lastFmResponse = jsonTranslator.mapToLastFmResponse(rawJson);
        List<LastFmTrack> tracks = lastFmResponse.getTracks();

        // then
        assertAll(
                () -> assertThat(lastFmResponse.getTotalCount()).isEqualTo(totalCount),
                () -> assertThat(tracks.stream()
                                       .filter(track -> track.getArtist().contains(artist))
                                       .count()).isEqualTo(totalCount),
                () -> assertThat(tracks.stream()
                                       .filter(track -> track.getTitle().contains(title))
                                       .count()).isEqualTo(totalCount)
        );
    }

    private static Stream<Arguments> searchLastFmResponseParameters() {
        return Stream.of(
                Arguments.of("김동률", "감사", 10)
        );
    }
}
