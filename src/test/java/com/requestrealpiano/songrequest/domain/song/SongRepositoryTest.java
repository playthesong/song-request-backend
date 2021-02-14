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
import static org.junit.jupiter.api.Assertions.assertAll;

class SongRepositoryTest extends BaseRepositoryTest {

    @Autowired
    SongRepository songRepository;

    @ParameterizedTest
    @MethodSource("findBySongTitleAndArtistContainingAllParameters")
    @DisplayName("OK - Song title, Artist 로 Song 을 찾는 테스트")
    void find_by_song_title_and_artist_containing_all(String songTitle, String artist) {
        // given
        List<Song> songs = createMockSongs();

        // when
        songRepository.saveAll(songs);
        Song song  = songRepository.findBySongTitleContainingIgnoreCaseAndArtistIgnoreCase(songTitle, artist)
                                   .orElseThrow(SongNotFoundException::new);

        // then
        assertAll(
                () -> assertThat(song.getSongTitle()).containsIgnoringCase(songTitle),
                () -> assertThat(song.getArtist()).isEqualToIgnoringCase(artist)
        );
    }

    private static Stream<Arguments> findBySongTitleAndArtistContainingAllParameters() {
        return Stream.of(
                Arguments.of("Something Just Like This", "Coldplay"),
                Arguments.of("Something just like", "coldplay"),
                Arguments.of("something just like this", "ColdPlay")
        );
    }

    private List<Song> createMockSongs() {
        Song firstSong = Song.builder().songTitle("Not exist title")
                                       .artist("Not exist artist")
                                       .imageUrl("Not exist image URL")
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
