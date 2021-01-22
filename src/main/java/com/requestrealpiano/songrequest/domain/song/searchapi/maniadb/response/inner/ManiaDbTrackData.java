package com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;

@Getter
@JacksonXmlRootElement(localName = "item")
public class ManiaDbTrackData {

    @JacksonXmlProperty(localName = "title")
    private String title;

    @JacksonXmlProperty(localName = "album")
    private ManiaDbAlbum album;

    @JacksonXmlProperty(localName = "artist")
    private ManiaDbArtist artist;
}
