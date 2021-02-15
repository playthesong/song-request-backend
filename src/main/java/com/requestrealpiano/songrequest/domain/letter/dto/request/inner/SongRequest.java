package com.requestrealpiano.songrequest.domain.letter.dto.request.inner;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SongRequest {

    private String title;
    private String artist;
    private String imageUrl;

    SongRequest(String title, String artist, String imageUrl) {
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
    }
}
