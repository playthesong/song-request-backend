package com.requestrealpiano.songrequest.testobject;

import com.requestrealpiano.songrequest.domain.song.Song;

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
}
