package com.requestrealpiano.songrequest.domain.song;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SongTest {

    @ParameterizedTest
    @MethodSource("createNewSongParameters")
    @DisplayName("새로운 음원 데이터를 생성한다.")
    void create_new_song(String songTitle, String albumTitle, String artist, String imageUrl, Integer requestCount,
                         String hashId) {
        // given
        Song song = Song.builder()
                        .songTitle(songTitle)
                        .albumTitle(albumTitle)
                        .artist(artist)
                        .imageUrl(imageUrl)
                        .requestCount(requestCount)
                        .hashId(hashId)
                        .build();

        // then
        assertAll(
                () -> assertThat(song).isNotNull(),
                () -> assertThat(song.getSongTitle()).isEqualTo(songTitle),
                () -> assertThat(song.getAlbumTitle()).isEqualTo(albumTitle),
                () -> assertThat(song.getArtist()).isEqualTo(artist),
                () -> assertThat(song.getRequestCount()).isEqualTo(requestCount),
                () -> assertThat(song.getHashId()).isEqualTo(hashId),
                () -> assertThat(song.getLetters()).isNotNull(),
                () -> assertThat(song.getYoutubeContent()).isNotNull()
        );
    }

    private static Stream<Arguments> createNewSongParameters() {
        return Stream.of(
                Arguments.of("노래 제목1", "앨범 이름1", "가수 이름1", "http://imageUrl_1", 1, "SaltedHashValue1"),
                Arguments.of("노래 제목2", "앨범 이름2", "가수 이름2", "http://imageUrl_2", 10, "SaltedHashValue2"),
                Arguments.of("노래 제목3", "앨범 이름3", "가수 이름3", "http://imageUrl_3", 16, "SaltedHashValue3")
        );
    }

}
