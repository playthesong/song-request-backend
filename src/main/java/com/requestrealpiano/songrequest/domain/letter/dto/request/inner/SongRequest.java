package com.requestrealpiano.songrequest.domain.letter.dto.request.inner;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SongRequest {

    @NotEmpty
    @Size(min = 1, max = 50)
    private String title;

    @NotEmpty
    @Size(min = 1, max = 30)
    private String artist;

    @Size(max = 100)
    private String imageUrl;

    SongRequest(String title, String artist, String imageUrl) {
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
    }
}
