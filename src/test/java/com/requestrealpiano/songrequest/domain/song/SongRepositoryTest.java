package com.requestrealpiano.songrequest.domain.song;

import com.requestrealpiano.songrequest.global.error.SongNotFoundException;
import com.requestrealpiano.songrequest.testconfig.BaseRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class SongRepositoryTest extends BaseRepositoryTest {

    @Autowired
    SongRepository songRepository;

    @ParameterizedTest
    @MethodSource("findExistSongByTitleAndArtistWhenExistParameters")
    @DisplayName("OK - Song title, Artist 로 Song 을 찾는 테스트")
    void find_by_title_and_artist_when_exist(String songTitle, String artist) {
        // given
        List<Song> songs = createMockSongs();

        // when
        songRepository.saveAll(songs);
        Song song = songRepository.findBySongTitleContainingIgnoreCaseAndArtistIgnoreCase(songTitle, artist)
                                   .orElseThrow(SongNotFoundException::new);

        // then
        assertAll(
                () -> assertThat(song.getSongTitle()).containsIgnoringCase(songTitle),
                () -> assertThat(song.getArtist()).isEqualToIgnoringCase(artist)
        );
    }

    private static Stream<Arguments> findExistSongByTitleAndArtistWhenExistParameters() {
        return Stream.of(
                Arguments.of("Something Just Like This", "Coldplay"),
                Arguments.of("Something just like", "coldplay"),
                Arguments.of("something just like this", "ColdPlay")
        );
    }

    @ParameterizedTest
    @MethodSource("findByTitleAndArtistWhenNotExistParameters")
    @DisplayName("Not found - 존재하지 않는 Song을 찾는 경우")
    void find_by_title_and_artist_when_not_exist(String title, String artist) {
        // given
        List<Song> songs = createMockSongs();

        // when
        songRepository.saveAll(songs);

        // then
        assertThatThrownBy(() -> songRepository.findBySongTitleContainingIgnoreCaseAndArtistIgnoreCase(title, artist)
                                               .orElseThrow(SongNotFoundException::new))
        .isInstanceOf(SongNotFoundException.class);
    }

    private static Stream<Arguments> findByTitleAndArtistWhenNotExistParameters() {
        return Stream.of(
                Arguments.of("Not exist title", "Not exist artist"),
                Arguments.of("Something not exist title", "Something not exist artist")
        );
    }

    private List<Song> createMockSongs() {
        Song firstSong = Song.builder().songTitle("exist title")
                                       .artist("exist artist")
                                       .imageUrl("exist image URL")
                                       .build();
        Song secondSong = Song.builder().songTitle("Hey Jude")
                                        .artist("The Beatles")
                                        .imageUrl("http://thebeatles.img")
                                        .build();
        Song thirdSong = Song.builder().songTitle("Something Just Like This")
                                       .artist("Coldplay")
                                       .imageUrl("http://coldplay.img")
                                       .build();
        return List.of(firstSong, secondSong, thirdSong);
    }
}
