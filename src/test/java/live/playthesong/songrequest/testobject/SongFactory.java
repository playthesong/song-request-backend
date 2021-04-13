package live.playthesong.songrequest.testobject;

import live.playthesong.songrequest.domain.letter.request.inner.SongRequest;
import live.playthesong.songrequest.domain.letter.request.inner.SongRequestBuilder;
import live.playthesong.songrequest.domain.song.Song;
import live.playthesong.songrequest.searchapi.request.SearchSongParameters;

import java.util.List;

public class SongFactory {

    /*
     *
     * createMockObject()
     *   - Test parameter 에 의존하지 않는 테스트 객체 생성
     *     (ex. Mocking 에서 자주 사용되는 테스트 객체)
     *
     *
     * createMockObjectOf(T parameter1, T parameter2, ...)
     *   - Test parameter 에 의존하는 테스트 객체 생성
     *     (ex. 예외 검증, 경우의 수를 따져야 하는 테스트)
     *
     */

    // Song
    public static Song createSong() {
        return Song.builder()
                   .songTitle("Song title")
                   .albumTitle("Album title")
                   .artist("Artist")
                   .imageUrl("http://imageUrl")
                   .requestCount(1)
                   .build();

    }

    public static Song createSongOf(Long id) {
        return Song.builder()
                   .id(id)
                   .songTitle("Song title")
                   .albumTitle("Album title")
                   .artist("Artist")
                   .imageUrl("http://imageUrl")
                   .requestCount(1)
                   .build();

    }

    public static Song createSongOf(int requestCount) {
        return Song.builder()
                   .songTitle("Song title")
                   .albumTitle("Album title")
                   .artist("Artist")
                   .imageUrl("http://imageUrl")
                   .requestCount(requestCount)
                   .build();

    }

    public static Song createSongOf(Long id, String title, String artist) {
        return Song.builder()
                   .id(id)
                   .songTitle(title)
                   .albumTitle("Album title")
                   .artist(artist)
                   .imageUrl("http://imageUrl")
                   .requestCount(1)
                   .build();
    }

    public static Song createSongOf(String songTitle, String artist, String imageUrl) {
        return Song.builder()
                   .songTitle(songTitle)
                   .albumTitle("Album title")
                   .artist(artist)
                   .imageUrl(imageUrl)
                   .requestCount(1)
                   .build();
    }

    // Songs
    public static List<Song> createSongs() {
        Song firstSong = Song.builder()
                             .songTitle("Wonderwall")
                             .artist("Oasis")
                             .imageUrl("http://oasis.img")
                             .build();
        Song secondSong = Song.builder()
                              .songTitle("Hey Jude")
                              .artist("The Beatles")
                              .imageUrl("http://thebeatles.img")
                              .build();
        Song thirdSong = Song.builder()
                             .songTitle("Something Just Like This")
                             .artist("Coldplay")
                             .imageUrl("http://coldplay.img")
                             .build();
        return List.of(firstSong, secondSong, thirdSong);
    }

    // SongRequest
    public static SongRequest createSongRequest() {
        return SongRequestBuilder.newBuilder()
                                 .title("New Title")
                                 .artist("New Artist")
                                 .imageUrl("New Image URL")
                                 .build();
    }

    public static SongRequest createSongRequestOf(String title, String artist, String imageUrl) {
        return SongRequestBuilder.newBuilder()
                                 .title(title)
                                 .artist(artist)
                                 .imageUrl(imageUrl)
                                 .build();
    }

    // SearchSongParameters
    public static SearchSongParameters createSearchSongParametersOf(String artist, String title) {
        SearchSongParameters parameters = new SearchSongParameters();
        parameters.setArtist(artist);
        parameters.setTitle(title);
        return parameters;
    }
}
