package com.requestrealpiano.songrequest.domain.song;

import com.requestrealpiano.songrequest.global.error.SongNotFoundException;
import com.requestrealpiano.songrequest.testconfig.BaseRepositoryTest;
import com.requestrealpiano.songrequest.testobject.SongFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @Test
    @DisplayName("OK - (Normal) Song title, Artist 로 Song 을 찾는 테스트")
    void find_by_title_and_artist_when_exist() {
        // given
        Song song = SongFactory.createOne();
        String title = song.getSongTitle();
        String artist = song.getArtist();

        // when
        songRepository.save(song);
        Song foundSong = songRepository.findBySongTitleContainingIgnoreCaseAndArtistIgnoreCase(title, artist)
                .orElseThrow(SongNotFoundException::new);

        // then
        assertAll(
                () -> assertThat(foundSong.getSongTitle()).containsIgnoringCase(title),
                () -> assertThat(foundSong.getArtist()).isEqualToIgnoringCase(artist)
        );
    }

    @Test
    @DisplayName("OK - (LowerCase) Song title, Artist 로 Song 을 찾는 테스트")
    void find_by_lower_case_title_and_artist_when_exist() {
        // given
        Song song = SongFactory.createOne();
        String lowerCasedTitle = song.getSongTitle().toLowerCase();
        String lowerCasedArtist = song.getArtist().toLowerCase();

        // when
        songRepository.save(song);
        Song foundSong = songRepository.findBySongTitleContainingIgnoreCaseAndArtistIgnoreCase(lowerCasedTitle, lowerCasedArtist)
                                       .orElseThrow(SongNotFoundException::new);

        // then
        assertAll(
                () -> assertThat(foundSong.getSongTitle()).containsIgnoringCase(lowerCasedTitle),
                () -> assertThat(foundSong.getArtist()).isEqualToIgnoringCase(lowerCasedArtist)
        );
    }

    @Test
    @DisplayName("OK - (Substring, Lowercase) Song title, Artist 로 Song 을 찾는 테스트")
    void find_by_substring_title_and_lowercase_artist_when_exist() {
        // given
        Song song = SongFactory.createOne();
        int beginIndex = 5;
        String subStringTitle = song.getSongTitle().substring(beginIndex);
        String lowerCasedArtist = song.getArtist().toLowerCase();

        // when
        songRepository.save(song);
        Song foundSong = songRepository.findBySongTitleContainingIgnoreCaseAndArtistIgnoreCase(subStringTitle, lowerCasedArtist)
                                       .orElseThrow(SongNotFoundException::new);

        // then
        assertAll(
                () -> assertThat(foundSong.getSongTitle()).containsIgnoringCase(subStringTitle),
                () -> assertThat(foundSong.getArtist()).isEqualToIgnoringCase(lowerCasedArtist)
        );
    }

    @ParameterizedTest
    @MethodSource("findByTitleAndArtistWhenNotExistParameters")
    @DisplayName("Not found - 존재하지 않는 Song을 찾는 경우")
    void find_by_title_and_artist_when_not_exist(String title, String artist) {
        // given
        List<Song> songs = SongFactory.createList();

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
}
