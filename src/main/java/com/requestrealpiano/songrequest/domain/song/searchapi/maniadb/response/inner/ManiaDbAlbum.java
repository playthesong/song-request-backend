package com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;

@Getter
@JacksonXmlRootElement(localName = "maniadb:album")
public class ManiaDbAlbum {

    @JacksonXmlProperty(localName = "title")
    private String title;

    @JacksonXmlProperty(localName = "image")
    private String imageUrl;
}
