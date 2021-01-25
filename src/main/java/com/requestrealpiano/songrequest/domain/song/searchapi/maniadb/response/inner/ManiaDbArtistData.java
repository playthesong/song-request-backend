package com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.inner;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@XmlRootElement(name = "manidb:artist")
public class ManiaDbArtistData {

    @XmlElement(name = "name")
    private String name;

    ManiaDbArtistData(String name) {
        this.name = name;
    }
}
