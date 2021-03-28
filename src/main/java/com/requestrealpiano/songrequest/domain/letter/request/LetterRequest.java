package com.requestrealpiano.songrequest.domain.letter.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import static com.requestrealpiano.songrequest.global.constant.ValidationCondition.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LetterRequest {

    @Size(max = SONG_STORY_MAX, message = SONG_STORY_MESSAGE)
    private String songStory;

    @Valid
    @JsonProperty("song")
    private SongRequest songRequest;

    LetterRequest(String songStory, SongRequest songRequest) {
        this.songStory = songStory;
        this.songRequest = songRequest;
    }
}
