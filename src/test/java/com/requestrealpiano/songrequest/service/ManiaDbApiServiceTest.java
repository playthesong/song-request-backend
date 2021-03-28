package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.ManiaDbRestClient;
import com.requestrealpiano.songrequest.domain.song.searchapi.request.SearchSongParameters;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.inner.Track;
import com.requestrealpiano.songrequest.domain.song.searchapi.translator.XmlTranslator;
import com.requestrealpiano.songrequest.service.searchapi.ManiaDbApiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static com.requestrealpiano.songrequest.testobject.SongFactory.createSearchSongParametersOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ManiaDbApiServiceTest {

    @InjectMocks
    ManiaDbApiService maniaDbApiService;

    @Mock
    ManiaDbRestClient maniaDbRestClient;

    @Spy
    XmlTranslator xmlTranslator;

    @ParameterizedTest
    @MethodSource("searchManiaDbResponseParameters")
    @DisplayName("ManiaDB 검색 결과 반환 테스트")
    void search_maniaDb_response(Path xmlPath, String artist, String title, int first, int totalCount) throws IOException {
        // given
        SearchSongParameters parameters = createSearchSongParametersOf(artist, title);
        String testXmlResponse = Files.readString(xmlPath);

        // when
        when(maniaDbRestClient.searchManiaDb(parameters)).thenReturn(testXmlResponse);
        SearchApiResponse response = maniaDbApiService.requestSearchApiResponse(parameters);
        List<Track> tracks = response.getTracks();
        Track track = tracks.get(first);

        // then
        assertAll(
                () -> assertThat(response.getTotalCount()).isEqualTo(totalCount),
                () -> assertThat(track.getArtist()).contains(artist),
                () -> assertThat(track.getTitle()).contains(title)
        );
    }

    private static Stream<Arguments> searchManiaDbResponseParameters() {
        return Stream.of(
                Arguments.of(Path.of("src/test/resources/expectedresponse/maniadb/maniadb_response.xml"),
                             "김동률", "감사", 0, 10)
        );
    }
}
