package com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.inner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"title", "artist", "imageUrl"})
public class LastFmTrack {

    private String name;
    private String artist;
    private String url;

    @JsonProperty("title")
    public String getTitle() {
        return name;
    }

    @JsonProperty(value = "name", access = JsonProperty.Access.WRITE_ONLY)
    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    @JsonProperty("imageUrl")
    public String getImageUrl() {
        return url;
    }

    @JsonProperty(value = "url", access = JsonProperty.Access.WRITE_ONLY)
    public void setUrl(String url) {
        this.url = url;
    }

    LastFmTrack(String name, String artist, String url) {
        this.name = name;
        this.artist = artist;
        this.url = url;
    }
}
