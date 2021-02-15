package com.requestrealpiano.songrequest.domain.letter.dto.response.inner;

import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.testobject.SongFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SongSummaryTest {

    @Test
    @DisplayName("Song 객체를 통해 SongSummary DTO를 생성하는 테스트")
    void create_new_song_summary() {
        // given
        Song song = SongFactory.createOne();

        // when
        SongSummary songSummary = SongSummary.from(song);

        // then
        assertAll(
                () -> assertThat(songSummary.getTitle()).isEqualTo(song.getSongTitle()),
                () -> assertThat(songSummary.getArtist()).isEqualTo(song.getArtist()),
                () -> assertThat(songSummary.getImageUrl()).isEqualTo(song.getImageUrl())
        );
    }
}
