package com.requestrealpiano.songrequest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequestBuilder;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.domain.song.SongRepository;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.inner.LastFmTrack;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.inner.Track;
import com.requestrealpiano.songrequest.service.searchapi.SearchApiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    @Mock
    SearchApiService searchApiService;

    @ParameterizedTest
    @MethodSource("whenFindExistSongByRequestParameters")
    @DisplayName("요청 받은 Song이 DB에 존재할 경우 RequestCount를 증가 시킨 뒤 반환하는 테스트")
    void when_find_exist_song_by_request(String songTitle, String artist, String imageUrl, int beforeCount, int afterCount) {
        // given
        SongRequest songRequest = SongRequestBuilder.newBuilder()
                                                    .title(songTitle)
                                                    .artist(artist)
                                                    .imageUrl(imageUrl)
                                                    .build();

        Song existSong = Song.builder()
                             .songTitle(songTitle)
                             .artist(artist)
                             .requestCount(beforeCount)
                             .imageUrl(imageUrl)
                             .build();

        // when
        when(songRepository.findBySongTitleContainingIgnoreCaseAndArtistIgnoreCase(songRequest.getTitle(), songRequest.getArtist()))
                .thenReturn(Optional.of(existSong));
        Song song = songService.findSongByRequest(songRequest);

        // then
        assertAll(
                () -> assertThat(song.getSongTitle()).isEqualTo(existSong.getSongTitle()),
                () -> assertThat(song.getArtist()).isEqualTo(existSong.getArtist()),
                () -> assertThat(song.getImageUrl()).isEqualTo(existSong.getImageUrl()),
                () -> assertThat(song.getRequestCount()).isEqualTo(afterCount)
        );
    }

    private static Stream<Arguments> whenFindExistSongByRequestParameters() {
        return Stream.of(
                Arguments.of("Exist song title", "Exist artist", "Exist image URL", 1, 2)
        );
    }

    @ParameterizedTest
    @MethodSource("whenFindNotExistSongParameters")
    @DisplayName("요청 받은 Song이 DB에 존재하지 않을 경우 새로 저장한 뒤 반환하는 테스트")
    void when_find_not_exist_song(String songTitle, String artist, String imageUrl) {
        // given
        SongRequest songRequest = SongRequestBuilder.newBuilder()
                                                    .title(songTitle)
                                                    .artist(artist)
                                                    .imageUrl(imageUrl)
                                                    .build();

        Song newSong = Song.builder()
                           .songTitle(songTitle)
                           .artist(artist)
                           .imageUrl(imageUrl)
                           .build();

        // when
        when(songRepository.findBySongTitleContainingIgnoreCaseAndArtistIgnoreCase(songRequest.getTitle(), songRequest.getArtist()))
                .thenReturn(Optional.empty());
        when(songRepository.save(any(Song.class))).thenReturn(newSong);

        Song song = songService.findSongByRequest(songRequest);

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

    @ParameterizedTest
    @MethodSource("searchManiaDbParameters")
    @DisplayName("SearchApiService를 통해 받아온 ManiaDB 데이터 검색 결과를 반환 하는 테스트")
    void search_mania_db(String artist, String title, int totalCount) throws JsonProcessingException {
        // given
        List<Track> mockTracks = createMockManiaDbTracks();
        SearchApiResponse testResponse = SearchApiResponse.builder()
                                                          .totalCount(totalCount)
                                                          .tracks(mockTracks)
                                                          .build();

        // when
        when(searchApiService.requestSearchApiResponse(artist, title)).thenReturn(testResponse);
        SearchApiResponse maniaDbResponse = songService.searchSong(artist, title);

        // then
        assertAll(
                () -> assertThat(maniaDbResponse.getTotalCount()).isEqualTo(testResponse.getTotalCount()),
                () -> assertThat(maniaDbResponse.getTracks()
                                                .stream()
                                                .filter(maniaDbTrack -> maniaDbTrack.getArtist().contains(artist))
                                                .count()).isEqualTo(totalCount)
        );
    }

    private static Stream<Arguments> searchManiaDbParameters() {
        return Stream.of(
                Arguments.of("Artist", "Title", 3)
        );
    }

    private List<Track> createMockManiaDbTracks() {
        Track firstTrack = Track.builder()
                                .artist("Artist 1")
                                .title("Title 1")
                                .imageUrl("http://imageUrl_1")
                                .build();
        Track secondTrack = Track.builder()
                                 .artist("Artist 2")
                                 .title("Title 2")
                                 .imageUrl("http://imageUrl_2")
                                 .build();
        Track thirdTrack = Track.builder()
                                .artist("Artist 3")
                                .title("Title 3")
                                .imageUrl("http://imageUrl_3")
                                .build();
        return Arrays.asList(firstTrack, secondTrack, thirdTrack);
    }

    @ParameterizedTest
    @MethodSource("searchLastFmParameters")
    @DisplayName("SearchApiService로부터 받아온 LastFM 검색 결과 데이터를 반환하는 테스트")
    void search_last_fm(String notExistArtist, String notExistTitle) throws JsonProcessingException {
        // given
        List<LastFmTrack> tracks = Collections.emptyList();
        int totalCount = tracks.size();
        SearchApiResponse mockLastFmResponse = SearchApiResponse.from(tracks);

        // when
        when(searchApiService.requestSearchApiResponse(notExistArtist, notExistTitle)).thenReturn(mockLastFmResponse);
        SearchApiResponse lastFmResponse = songService.searchSong(notExistArtist, notExistTitle);

        // then
        assertThat(lastFmResponse.getTotalCount()).isEqualTo(totalCount);
    }

    private static Stream<Arguments> searchLastFmParameters() {
        return Stream.of(
                Arguments.of("This artist does not exist ", "This title does not exist")
        );
    }
}
