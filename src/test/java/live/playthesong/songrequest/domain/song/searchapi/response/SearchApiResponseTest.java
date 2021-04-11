package live.playthesong.songrequest.domain.song.searchapi.response;

import live.playthesong.songrequest.domain.song.searchapi.lastfm.response.inner.LastFmTrack;
import live.playthesong.songrequest.domain.song.searchapi.maniadb.response.ManiaDbClientResponse;
import live.playthesong.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbData;
import live.playthesong.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbTrackData;
import live.playthesong.songrequest.domain.song.searchapi.response.inner.Track;
import live.playthesong.songrequest.domain.song.searchapi.translator.XmlTranslator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SearchApiResponseTest {

    @InjectMocks
    XmlTranslator xmlTranslator;

    @ParameterizedTest
    @MethodSource("createFinalManiaDbResultParameters")
    @DisplayName("ManiaDB의 응답 데이터를 바탕으로 최종 검색 결과 DTO를 생성하는 테스트")
    void create_final_maniadb_result(Path testXmlPath) throws IOException {
        // given
        String testXml = Files.readString(testXmlPath);

        ManiaDbClientResponse maniaDbClientResponse = xmlTranslator.mapToManiaDbData(testXml);

        ManiaDbData data = maniaDbClientResponse.getManiaDbData();
        List<ManiaDbTrackData> trackDataList = data.getTracks();
        List<Track> tracks = trackDataList.stream()
                                          .map(Track::from)
                                          .collect(Collectors.toList());

        // when
        SearchApiResponse maniaDbResponse = SearchApiResponse.from(maniaDbClientResponse);
        List<Track> maniaDbTracks = maniaDbResponse.getTracks();

        // then
        assertAll(
                () -> assertThat(maniaDbResponse.getTotalCount()).isEqualTo(data.getTotalCount()),
                () -> assertThat(maniaDbTracks.size()).isEqualTo(tracks.size())
        );
    }

    private static Stream<Arguments> createFinalManiaDbResultParameters() {
        return Stream.of(
                Arguments.of(Path.of("src/test/resources/expectedresponse/maniadb/maniadb_response.xml"))
        );
    }

    @Test
    @DisplayName("정적 팩토리 메서드 from()을 사용하여 LastFm 검색 결과로부터 최종 응답 DTO를 생성하는 테스트")
    void create_new_lastfm_response() {
        // given
        List<LastFmTrack> tracks = new ArrayList<>();
        int totalCount = tracks.size();

        // when
        SearchApiResponse lastFmResponse = SearchApiResponse.from(tracks);

        // then
        assertThat(lastFmResponse.getTotalCount()).isEqualTo(totalCount);
        assertThat(lastFmResponse.getTracks()).hasOnlyElementsOfType(Track.class);
    }

    @Test
    @DisplayName("When Null - LastFmTracks 값이 null인 경우 대체 값을 생성하고 반환하는 테스트")
    void when_null_search_lastfm_response() {
        // given
        List<LastFmTrack> tracks = null;
        int totalCount = 0;

        // when
        SearchApiResponse lastFmResponse = SearchApiResponse.from(tracks);

        // then
        assertThatCode(() -> SearchApiResponse.from(tracks)).doesNotThrowAnyException();
        assertThat(lastFmResponse.getTotalCount()).isEqualTo(totalCount);
        assertThat(lastFmResponse.getTracks().size()).isEqualTo(totalCount);
    }
}
