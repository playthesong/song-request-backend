package com.requestrealpiano.songrequest.domain.song;

import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.global.error.exception.business.SongNotFoundException;
import com.requestrealpiano.songrequest.testconfig.BaseRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static com.requestrealpiano.songrequest.testobject.SongFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class SongRepositoryTest extends BaseRepositoryTest {

    @Autowired
    SongRepository songRepository;

    @Test
    @DisplayName("Song 생성 시 RequestCount 기본 값 설정 테스트")
    void create_song_default_request_count() {
        // given
        SongRequest songRequest = createSongRequest();
        Song song = Song.from(songRequest);
        int defaultRequestCount = 1;

        // when
        Song newSong = songRepository.save(song);

        // then
        assertThat(newSong.getRequestCount()).isNotNull();
        assertThat(newSong.getRequestCount()).isEqualTo(defaultRequestCount);
    }

    @ParameterizedTest
    @MethodSource("findByTitleAndArtistParameters")
    @DisplayName("OK - Song title, Artist 로 Song 을 찾는 테스트")
    void find_by_title_and_artist_when_exist(Song song, String title, String artist) {
        // when
        songRepository.save(song);
        Song foundSong = songRepository.findBySongTitleAndArtist(title, artist)
                                       .orElseThrow(SongNotFoundException::new);

        // then
        assertAll(
                () -> assertThat(foundSong.getSongTitle()).containsIgnoringCase(title),
                () -> assertThat(foundSong.getArtist()).isEqualToIgnoringCase(artist)
        );
    }

    @ParameterizedTest
    @MethodSource("findByTitleAndArtistWhenNotExistParameters")
    @DisplayName("NOT_FOUND - 존재하지 않는 Song을 찾는 경우")
    void find_by_title_and_artist_when_not_exist(String title, String artist) {
        // given
        List<Song> songs = createSongs();

        // when
        songRepository.saveAll(songs);

        // then
        assertThatThrownBy(() -> songRepository.findBySongTitleAndArtist(title, artist)
                                               .orElseThrow(SongNotFoundException::new))
        .isInstanceOf(SongNotFoundException.class);
    }

    private static Stream<Arguments> findByTitleAndArtistParameters() {
        // Normal
        Song song = createSong();
        String title = song.getSongTitle();
        String artist = song.getArtist();

        // LowerCase
        String lowerCasedTitle = song.getSongTitle().toLowerCase();
        String lowerCasedArtist = song.getArtist().toLowerCase();

        // SubString
        int beginIndex = 5;
        String subStringTitle = song.getSongTitle().substring(beginIndex);

        return Stream.of(
                Arguments.of(song, title, artist),
                Arguments.of(song, title, lowerCasedArtist),
                Arguments.of(song, lowerCasedTitle, lowerCasedArtist),
                Arguments.of(song, subStringTitle, artist),
                Arguments.of(song, subStringTitle, lowerCasedArtist)
        );
    }

    private static Stream<Arguments> findByTitleAndArtistWhenNotExistParameters() {
        return Stream.of(
                Arguments.of("Not exist title", "Not exist artist"),
                Arguments.of("Something not exist title", "Something not exist artist")
        );
    }
}
