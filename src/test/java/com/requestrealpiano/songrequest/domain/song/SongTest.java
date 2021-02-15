package com.requestrealpiano.songrequest.domain.song;

import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SongTest {

    @ParameterizedTest
    @MethodSource("createNewSongParameters")
    @DisplayName("새로운 음원 데이터를 생성한다.")
    void create_new_song(String songTitle, String albumTitle, String artist, String imageUrl, Integer requestCount) {
        // given
        Song song = Song.builder()
                        .songTitle(songTitle)
                        .albumTitle(albumTitle)
                        .artist(artist)
                        .imageUrl(imageUrl)
                        .requestCount(requestCount)
                        .build();

        // then
        assertAll(
                () -> assertThat(song).isNotNull(),
                () -> assertThat(song.getSongTitle()).isEqualTo(songTitle),
                () -> assertThat(song.getAlbumTitle()).isEqualTo(albumTitle),
                () -> assertThat(song.getArtist()).isEqualTo(artist),
                () -> assertThat(song.getRequestCount()).isEqualTo(requestCount),
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

    @ParameterizedTest
    @MethodSource("createNewSongByRequest")
    @DisplayName("DTO (SongRequest) 로부터 새로운 Song 을 생성하는 테스트")
    void create_new_song_by_request(String title, String artist, String imageUrl, int initialCount) {
        // given
        SongRequest songRequest = SongRequestBuilder.newBuilder()
                                                    .title(title)
                                                    .artist(artist)
                                                    .imageUrl(imageUrl)
                                                    .build();

        // when
        Song song = Song.from(songRequest);

        // then
        assertAll(
                () -> assertThat(song.getSongTitle()).isEqualTo(songRequest.getTitle()),
                () -> assertThat(song.getArtist()).isEqualTo(songRequest.getArtist()),
                () -> assertThat(song.getImageUrl()).isEqualTo(songRequest.getImageUrl()),
                () -> assertThat(song.getRequestCount()).isEqualTo(initialCount)
        );
    }

    private static Stream<Arguments> createNewSongByRequest() {
        return Stream.of(
                Arguments.of("New Title", "New Artist", "New Image URL", 1)
        );
    }

    @ParameterizedTest
    @MethodSource("increaseRequestCountParameters")
    @DisplayName("requestCount 증가 메서드 테스트")
    void increase_request_count(String title, String artist, String imageUrl, int initialCount, int increasedCount) {
        // given
        Song song = Song.builder()
                        .songTitle(title)
                        .artist(artist)
                        .imageUrl(imageUrl)
                        .requestCount(initialCount)
                        .build();

        // when
        Song updatedSong = song.increaseRequestCount();

        // then
        assertThat(updatedSong.getRequestCount()).isEqualTo(increasedCount);
    }

    private static Stream<Arguments> increaseRequestCountParameters() {
        return Stream.of(
                Arguments.of("Song title", "Artist", "http://ImageUrl", 1, 2)
        );
    }
}
