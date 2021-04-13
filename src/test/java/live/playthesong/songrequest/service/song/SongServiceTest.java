package live.playthesong.songrequest.service.song;

import live.playthesong.songrequest.domain.letter.request.PaginationParameters;
import live.playthesong.songrequest.domain.letter.request.inner.SongRequest;
import live.playthesong.songrequest.domain.song.Song;
import live.playthesong.songrequest.domain.song.SongRepository;
import live.playthesong.songrequest.domain.song.response.SongRankingResponse;
import live.playthesong.songrequest.service.SongService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static live.playthesong.songrequest.global.constant.SortProperties.REQUEST_COUNT;
import static live.playthesong.songrequest.testobject.PaginationFactory.*;
import static live.playthesong.songrequest.testobject.SongFactory.createSongOf;
import static live.playthesong.songrequest.testobject.SongFactory.createSongRequestOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

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
        when(songRepository.findBySongTitleAndArtist(songRequest.getTitle(), songRequest.getArtist()))
                .thenReturn(List.of(existSong));
        Song song = songService.updateRequestCountOrElseCreate(songRequest);

        // then
        assertAll(
                () -> assertThat(song.getSongTitle()).isEqualTo(existSong.getSongTitle()),
                () -> assertThat(song.getArtist()).isEqualTo(existSong.getArtist()),
                () -> assertThat(song.getImageUrl()).isEqualTo(existSong.getImageUrl()),
                () -> assertThat(song.getRequestCount()).isEqualTo(increasedCount)
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
        when(songRepository.findBySongTitleAndArtist(songRequest.getTitle(), songRequest.getArtist()))
                .thenReturn(Collections.emptyList());
        when(songRepository.save(any(Song.class))).thenReturn(newSong);

        Song song = songService.updateRequestCountOrElseCreate(songRequest);

        // then
        assertAll(
                () -> assertThat(song.getSongTitle()).isEqualTo(newSong.getSongTitle()),
                () -> assertThat(song.getArtist()).isEqualTo(newSong.getArtist()),
                () -> assertThat(song.getImageUrl()).isEqualTo(newSong.getImageUrl())
        );
    }

    @ParameterizedTest
    @MethodSource("findSongsParameters")
    @DisplayName("Song 랭킹 조회 테스트")
    void find_songs(int page, int size, int minRequestCount, int maxRequestCount, List<Song> songs) {
        // given
        int first = 0;
        int last = songs.size() - 1;
        PaginationParameters parameters = createPaginationParametersOf(page, size, DESC.name());

        // when
        when(songRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(songs));
        SongRankingResponse rankingResponse = songService.findSongs(parameters);

        // then
        assertAll(
                () -> assertThat(rankingResponse.getSongs()).size().isEqualTo(size),
                () -> assertThat(rankingResponse.getSongs().get(first).getRequestCount()).isEqualTo(minRequestCount),
                () -> assertThat(rankingResponse.getSongs().get(last).getRequestCount()).isEqualTo(maxRequestCount)
        );
    }

    private static Stream<Arguments> whenFindExistSongByRequestParameters() {
        return Stream.of(
                Arguments.of("Exist song title", "Exist artist", "Exist image URL")
        );
    }

    private static Stream<Arguments> whenFindNotExistSongParameters() {
        return Stream.of(
                Arguments.of("Not exist song title", "Not exist artist", "Not exist")
        );
    }

    private static Stream<Arguments> findSongsParameters() {
        int minRequestCount = 1;
        int maxRequestCount = 30;
        List<Song> songs = new ArrayList<>();

        for (int i = minRequestCount; i <= maxRequestCount; i++) {
            songs.add(createSongOf((long) i, i));
        }

        return Stream.of(
                Arguments.of(0, 30, minRequestCount, maxRequestCount, songs)
        );
    }
}
