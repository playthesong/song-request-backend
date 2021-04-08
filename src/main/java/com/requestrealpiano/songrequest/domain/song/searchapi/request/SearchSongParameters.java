package com.requestrealpiano.songrequest.domain.song.searchapi.request;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Size;

import static com.requestrealpiano.songrequest.global.constant.ValidationCondition.*;

@Getter
public class SearchSongParameters {

    @Size(max = ARTIST_MAX, message = ARTIST_MESSAGE)
    private String artist;

    @Size(max = TITLE_MAX, message = TITLE_MESSAGE)
    private String title;

    public void setArtist(String artist) {
        if (StringUtils.isEmpty(artist)) {
            this.artist = StringUtils.SPACE;
            return;
        }
        this.artist = artist;
    }

    public void setTitle(String title) {
        if (StringUtils.isEmpty(title)) {
            this.title = StringUtils.SPACE;
            return;
        }
        this.title = title;
    }
}
