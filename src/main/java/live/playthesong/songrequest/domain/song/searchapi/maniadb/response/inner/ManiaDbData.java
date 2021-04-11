package live.playthesong.songrequest.domain.song.searchapi.maniadb.response.inner;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;

import java.util.List;

@Getter
@JacksonXmlRootElement(localName = "channel")
public class ManiaDbData {

    @JacksonXmlProperty(localName = "total")
    private int totalCount;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    private List<ManiaDbTrackData> tracks;
}
