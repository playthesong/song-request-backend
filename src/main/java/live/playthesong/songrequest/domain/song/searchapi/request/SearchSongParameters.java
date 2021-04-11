package live.playthesong.songrequest.domain.song.searchapi.request;

import live.playthesong.songrequest.global.constant.ValidationCondition;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Size;

@Getter
public class SearchSongParameters {

    @Size(max = ValidationCondition.ARTIST_MAX, message = ValidationCondition.ARTIST_MESSAGE)
    private String artist;

    @Size(max = ValidationCondition.TITLE_MAX, message = ValidationCondition.TITLE_MESSAGE)
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
