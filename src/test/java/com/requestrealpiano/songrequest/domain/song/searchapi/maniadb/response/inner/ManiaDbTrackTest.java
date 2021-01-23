package com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ManiaDbTrackTest {

    @ParameterizedTest
    @MethodSource("createNewManiaDbTrackParameters")
    @DisplayName("ManiaDB의 Track 데이터를 이용하여 응답 Track 데이터를 생성하는 테스트")
    void create_new_maniadb_track(String albumTitle, String imageUrl, String artistName, String songTitle) {
        // given
        ManiaDbAlbumData albumData = new ManiaDbAlbumData(albumTitle, imageUrl);
        ManiaDbArtistData artistData = new ManiaDbArtistData(artistName);
        ManiaDbTrackData trackData = new ManiaDbTrackData(songTitle, albumData, artistData);

        // when
        ManiaDbTrack track = ManiaDbTrack.from(trackData);

        // then
        assertAll(
                () -> assertThat(track.getTitle()).isEqualTo(trackData.getTitle()),
                () -> assertThat(track.getImageUrl()).isEqualTo(albumData.getImageUrl()),
                () -> assertThat(track.getArtist()).isEqualTo(artistData.getName())
        );
    }

    private static Stream<Arguments> createNewManiaDbTrackParameters() {
        return Stream.of(
                Arguments.of("Album Title 1", "Image URL 1", "Artist Name 1", "Song Title 1"),
                Arguments.of("Album Title 2", "Image URL 2", "Artist Name 2", "Song Title 2"),
                Arguments.of("Album Title 3", "Image URL 3", "Artist Name 3", "Song Title 3")
        );
    }
}
