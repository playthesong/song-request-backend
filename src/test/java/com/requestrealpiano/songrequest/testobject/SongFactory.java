package com.requestrealpiano.songrequest.testobject;

import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequestBuilder;
import com.requestrealpiano.songrequest.domain.song.Song;

import java.util.List;

public class SongFactory {

    public static Song createOne() {
        return Song.builder()
                   .songTitle("Song title")
                   .albumTitle("Album title")
                   .artist("Artist")
                   .imageUrl("http://imageUrl")
                   .requestCount(1)
                   .build();

    }

    public static List<Song> createList() {
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

    public static SongRequest createRequest() {
        return SongRequestBuilder.newBuilder()
                                 .title("New Title")
                                 .artist("New Artist")
                                 .imageUrl("New Image URL")
                                 .build();
    }
}
