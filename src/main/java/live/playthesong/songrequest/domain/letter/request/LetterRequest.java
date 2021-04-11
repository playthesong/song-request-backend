package live.playthesong.songrequest.domain.letter.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.playthesong.songrequest.domain.letter.request.inner.SongRequest;
import live.playthesong.songrequest.global.constant.ValidationCondition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LetterRequest {

    @Size(max = ValidationCondition.SONG_STORY_MAX, message = ValidationCondition.SONG_STORY_MESSAGE)
    private String songStory;

    @Valid
    @JsonProperty("song")
    private SongRequest songRequest;

    LetterRequest(String songStory, SongRequest songRequest) {
        this.songStory = songStory;
        this.songRequest = songRequest;
    }
}
