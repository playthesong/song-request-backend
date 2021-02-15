package com.requestrealpiano.songrequest.domain.letter.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NewLetterRequest {

    private String songStory;

    @JsonProperty("song")
    private SongRequest songRequest;

    private Long accountId;

    NewLetterRequest(String songStory, SongRequest songRequest, Long accountId) {
        this.songStory = songStory;
        this.songRequest = songRequest;
        this.accountId = accountId;
    }
}
