package com.requestrealpiano.songrequest.domain.song.searchapi.maniadb;

import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbClientResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbAlbumData;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbArtistData;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbData;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbTrackData;
import com.requestrealpiano.songrequest.domain.song.searchapi.translator.XmlTranslator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class XmlTranslatorTest {

    @InjectMocks
    XmlTranslator xmlTranslator;

    @ParameterizedTest
    @MethodSource("mapXmlResponseToDtoParameters")
    @DisplayName("ManiaDB API 응답 XML을 DTO에 매핑하는 테스트")
    void map_xml_response_to_dto(String xmlPath, int first, int totalCount, int trackCount) throws IOException {
        // given
        Path testXmlFilePath = Path.of(xmlPath);
        String testXml = Files.readString(testXmlFilePath);

        // when
        ManiaDbClientResponse maniaDbClientResponse = xmlTranslator.mapToManiaDbData(testXml);
        ManiaDbData maniaDbData = maniaDbClientResponse.getManiaDbData();
        List<ManiaDbTrackData> tracks = maniaDbData.getTracks();

        ManiaDbTrackData track = tracks.get(first);
        ManiaDbAlbumData album = track.getAlbumData();
        ManiaDbArtistData artist = track.getArtistData();

        // then
        assertAll(
                () -> assertThat(maniaDbClientResponse).isNotNull(),
                () -> assertThat(maniaDbData.getTotalCount()).isEqualTo(totalCount),
                () -> assertThat(tracks.size()).isEqualTo(trackCount),
                () -> assertThat(album).isNotNull(),
                () -> assertThat(album.getTitle()).isNotEmpty(),
                () -> assertThat(album.getImageUrl()).isNotEmpty(),
                () -> assertThat(artist).isNotNull(),
                () -> assertThat(artist.getName()).isNotEmpty()
        );
    }

    private static Stream<Arguments> mapXmlResponseToDtoParameters() {
        return Stream.of(
                Arguments.of("src/test/resources/expectedresponse/maniadb/maniadb_response.xml", 0, 10, 10)
        );
    }
}
