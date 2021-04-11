package live.playthesong.songrequest.searchapi.maniadb.response.inner;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@JacksonXmlRootElement(localName = "maniadb:album")
public class ManiaDbAlbumData {

    @JacksonXmlProperty(localName = "title")
    private String title;

    @JacksonXmlProperty(localName = "image")
    private String imageUrl;

    ManiaDbAlbumData(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }
}
