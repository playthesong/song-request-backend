package com.requestrealpiano.songrequest.domain.letter.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NewLetterRequest {

    @Size(max = 500, message = "사연은 500자 미만이어야 합니다.")
    private String songStory;

    @Valid
    @JsonProperty("song")
    private SongRequest songRequest;

    private Long accountId;

    NewLetterRequest(String songStory, SongRequest songRequest, Long accountId) {
        this.songStory = songStory;
        this.songRequest = songRequest;
        this.accountId = accountId;
    }
}
