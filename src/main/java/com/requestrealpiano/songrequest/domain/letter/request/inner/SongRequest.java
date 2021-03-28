package com.requestrealpiano.songrequest.domain.letter.request.inner;

import com.requestrealpiano.songrequest.global.constant.ValidationCondition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static com.requestrealpiano.songrequest.global.constant.ValidationCondition.NOT_EMPTY_MESSAGE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SongRequest {

    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(min = ValidationCondition.TITLE_MIN, max = ValidationCondition.TITLE_MAX, message = ValidationCondition.TITLE_MESSAGE)
    private String title;

    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(min = ValidationCondition.ARTIST_MIN, max = ValidationCondition.ARTIST_MAX, message = ValidationCondition.ARTIST_MESSAGE)
    private String artist;

    @Size(max = ValidationCondition.IMAGE_MAX, message = ValidationCondition.IMAGE_MESSAGE)
    private String imageUrl;

    SongRequest(String title, String artist, String imageUrl) {
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
    }
}
