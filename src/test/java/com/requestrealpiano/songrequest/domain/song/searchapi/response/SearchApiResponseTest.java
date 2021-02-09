package com.requestrealpiano.songrequest.domain.song.searchapi.response;

import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.LastFmResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.inner.LastFmTrack;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbClientResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbData;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbTrack;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbTrackData;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.inner.Track;
import com.requestrealpiano.songrequest.domain.song.searchapi.translator.XmlTranslator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SearchApiResponseTest {

    @InjectMocks
    XmlTranslator xmlTranslator;

    @Test
    @DisplayName("ManiaDB의 응답 데이터를 바탕으로 최종 검색 결과 DTO를 생성하는 테스트")
    void create_final_result_maniadb_response() throws IOException {
        // given
        Path testXmlFilePath = Path.of("src/test/resources/expectedresponse/maniadb/maniadb_response.xml");
        String testXml = Files.readString(testXmlFilePath);

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
}
