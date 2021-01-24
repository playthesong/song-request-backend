package com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.inner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LastFmTrackTest {

    @ParameterizedTest
    @CsvSource({"Title, Artist, http://imageUrl"})
    @DisplayName("LastFm Track 데이터 직렬화 테스트")
    void serialize_lastfm_track(String title, String artist, String url) throws JsonProcessingException {
        // given
        LastFmTrack track = new LastFmTrack(title, artist, url);
        String testJson = "{\"title\":\"Title\",\"artist\":\"Artist\",\"imageUrl\":\"http://imageUrl\"}";
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        String json = objectMapper.writeValueAsString(track);

        // then
        assertThat(json).isEqualTo(testJson);
    }

    @ParameterizedTest
    @CsvSource({"Title, Artist, http://imageUrl"})
    @DisplayName("LastFm Track 데이터 역직렬화 테스트")
    void deserialize_lastfm_track(String title, String artist, String url) throws JsonProcessingException {
        // given
        LastFmTrack track = new LastFmTrack(title, artist, url);
        String testJson = "{\"name\":\"Title\",\"artist\":\"Artist\",\"url\":\"http://imageUrl\"}";
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        LastFmTrack testTrack = objectMapper.readValue(testJson, LastFmTrack.class);

        // then
        assertAll(
                () -> assertThat(track.getArtist()).isEqualTo(testTrack.getArtist()),
                () -> assertThat(track.getTitle()).isEqualTo(testTrack.getTitle()),
                () -> assertThat(track.getImageUrl()).isEqualTo(testTrack.getImageUrl())
        );
    }
}
