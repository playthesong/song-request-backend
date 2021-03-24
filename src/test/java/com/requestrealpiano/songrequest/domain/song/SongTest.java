package com.requestrealpiano.songrequest.domain.song;

import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.requestrealpiano.songrequest.testobject.SongFactory.createSong;
import static com.requestrealpiano.songrequest.testobject.SongFactory.createSongRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SongTest {

    @Test
    @DisplayName("SongRequest(DTO) 로부터 새로운 Song 을 생성하는 테스트")
    void create_new_song_by_request() {
        // given
        SongRequest songRequest = createSongRequest();

        // when
        Song song = Song.from(songRequest);

        // then
        assertAll(
                () -> assertThat(song.getSongTitle()).isEqualTo(songRequest.getTitle()),
                () -> assertThat(song.getArtist()).isEqualTo(songRequest.getArtist()),
                () -> assertThat(song.getImageUrl()).isEqualTo(songRequest.getImageUrl())
        );
    }

    @Test
    @DisplayName("requestCount 증가 메서드 테스트")
    void increase_request_count() {
        // given
        Song song = createSong();
        int initialCount = song.getRequestCount();
        int increasedCount = initialCount + 1;

        // when
        Song updatedSong = song.increaseRequestCount();

        // then
        assertThat(updatedSong.getRequestCount()).isEqualTo(increasedCount);
    }
}
