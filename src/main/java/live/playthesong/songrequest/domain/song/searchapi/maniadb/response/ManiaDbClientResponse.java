package live.playthesong.songrequest.domain.song.searchapi.maniadb.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import live.playthesong.songrequest.domain.song.searchapi.maniadb.response.inner.ManiaDbData;
import lombok.Getter;

@Getter
@JacksonXmlRootElement(localName = "rss")
public class ManiaDbClientResponse {

    @JacksonXmlProperty(localName = "channel")
    private ManiaDbData maniaDbData;
}
