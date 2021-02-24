package com.requestrealpiano.songrequest.domain.letter.request.inner;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static com.requestrealpiano.songrequest.global.constant.ValidationCondition.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SongRequest {

    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(min = TITLE_MIN, max = TITLE_MAX, message = TITLE_MESSAGE)
    private String title;

    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(min = ARTIST_MIN, max = ARTIST_MAX, message = ARTIST_MESSAGE)
    private String artist;

    @Size(max = IMAGE_MAX, message = IMAGE_MESSAGE)
    private String imageUrl;

    SongRequest(String title, String artist, String imageUrl) {
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
    }
}
