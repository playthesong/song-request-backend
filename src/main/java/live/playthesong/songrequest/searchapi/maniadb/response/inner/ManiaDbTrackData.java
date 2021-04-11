package live.playthesong.songrequest.searchapi.maniadb.response.inner;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@JacksonXmlRootElement(localName = "item")
public class ManiaDbTrackData {

    @JacksonXmlProperty(localName = "title")
    private String title;

    @JacksonXmlProperty(localName = "album")
    private ManiaDbAlbumData albumData;

    @JacksonXmlProperty(localName = "artist")
    private ManiaDbArtistData artistData;

    ManiaDbTrackData(String title, ManiaDbAlbumData albumData, ManiaDbArtistData artistData) {
        this.title = title;
        this.albumData = albumData;
        this.artistData = artistData;
    }
}
