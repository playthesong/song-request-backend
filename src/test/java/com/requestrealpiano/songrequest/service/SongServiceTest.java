package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.domain.song.SongRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static com.requestrealpiano.songrequest.testobject.SongFactory.createSongOf;
import static com.requestrealpiano.songrequest.testobject.SongFactory.createSongRequestOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SongServiceTest {

    @InjectMocks
    SongService songService;

    @Mock
    SongRepository songRepository;

    @ParameterizedTest
    @MethodSource("whenFindExistSongByRequestParameters")
    @DisplayName("요청 받은 Song이 DB에 존재할 경우 RequestCount를 증가 시킨 뒤 반환하는 테스트")
    void when_find_exist_song_by_request(String songTitle, String artist, String imageUrl) {
        // given
        SongRequest songRequest = createSongRequestOf(songTitle, artist, imageUrl);
        Song existSong = createSongOf(songTitle, artist, imageUrl);
        int initialCount = existSong.getRequestCount();
        int increasedCount = initialCount + 1;

        // when
        when(songRepository.findBySongTitleContainingIgnoreCaseAndArtistIgnoreCase(songRequest.getTitle(), songRequest.getArtist()))
                .thenReturn(Optional.of(existSong));
        Song song = songService.updateRequestCountOrElseCreate(songRequest);

        // then
        assertAll(
                () -> assertThat(song.getSongTitle()).isEqualTo(existSong.getSongTitle()),
                () -> assertThat(song.getArtist()).isEqualTo(existSong.getArtist()),
                () -> assertThat(song.getImageUrl()).isEqualTo(existSong.getImageUrl()),
                () -> assertThat(song.getRequestCount()).isEqualTo(increasedCount)
        );
    }

    private static Stream<Arguments> whenFindExistSongByRequestParameters() {
        return Stream.of(
                Arguments.of("Exist song title", "Exist artist", "Exist image URL")
        );
    }

    @ParameterizedTest
    @MethodSource("whenFindNotExistSongParameters")
    @DisplayName("요청 받은 Song이 DB에 존재하지 않을 경우 새로 저장한 뒤 반환하는 테스트")
    void when_find_not_exist_song(String songTitle, String artist, String imageUrl) {
        // given
        SongRequest songRequest = createSongRequestOf(songTitle, artist, imageUrl);
        Song newSong = createSongOf(songTitle, artist, imageUrl);

        // when
        when(songRepository.findBySongTitleContainingIgnoreCaseAndArtistIgnoreCase(songRequest.getTitle(), songRequest.getArtist()))
                .thenReturn(Optional.empty());
        when(songRepository.save(any(Song.class))).thenReturn(newSong);

        Song song = songService.updateRequestCountOrElseCreate(songRequest);

        // then
        assertAll(
                () -> assertThat(song.getSongTitle()).isEqualTo(newSong.getSongTitle()),
                () -> assertThat(song.getArtist()).isEqualTo(newSong.getArtist()),
                () -> assertThat(song.getImageUrl()).isEqualTo(newSong.getImageUrl())
        );
    }

    private static Stream<Arguments> whenFindNotExistSongParameters() {
        return Stream.of(
                Arguments.of("Not exist song title", "Not exist artist", "Not exist")
        );
    }
}
