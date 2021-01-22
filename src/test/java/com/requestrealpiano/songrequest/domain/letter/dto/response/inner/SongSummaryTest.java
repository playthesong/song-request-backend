package com.requestrealpiano.songrequest.domain.letter.dto.response.inner;

import com.requestrealpiano.songrequest.domain.song.Song;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SongSummaryTest {

    @ParameterizedTest
    @MethodSource("createNewSongSummaryParameters")
    @DisplayName("Song 객체를 통해 SongSummary DTO를 생성하는 테스트")
    void create_new_song_summary(String songTitle, String albumTitle, String artist, String imageUrl,
                                 Integer requestCount) {
        // given
        Song song = Song.builder()
                        .songTitle(songTitle)
                        .albumTitle(albumTitle)
                        .artist(artist)
                        .imageUrl(imageUrl)
                        .requestCount(requestCount)
                        .build();

        // when
        SongSummary songSummary = SongSummary.from(song);

        // then
        assertAll(
                () -> assertThat(songSummary.getTitle()).isEqualTo(songTitle),
                () -> assertThat(songSummary.getArtist()).isEqualTo(artist),
                () -> assertThat(songSummary.getImageUrl()).isEqualTo(imageUrl)
        );

    }

    private static Stream<Arguments> createNewSongSummaryParameters() {
        return Stream.of(
                Arguments.of("Song Title 1", "Album Title 1", "Artist Name 1", "http://imageUrl_1", 10),
                Arguments.of("Song Title 2", "Album Title 2", "Artist Name 2", "http://imageUrl_2", 15),
                Arguments.of("Song Title 3", "Album Title 3", "Artist Name 3", "http://imageUrl_3", 17)
        );
    }
}
