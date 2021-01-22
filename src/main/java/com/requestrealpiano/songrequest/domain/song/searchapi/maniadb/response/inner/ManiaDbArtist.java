package com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner;

import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@XmlRootElement(name = "manidb:artist")
public class ManiaDbArtist {

    @XmlElement(name = "name")
    private String name;
}
