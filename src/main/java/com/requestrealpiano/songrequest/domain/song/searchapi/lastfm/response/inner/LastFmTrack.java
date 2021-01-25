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

    private String title;
    private String artist;
    private String imageUrl;

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty(value = "name", access = JsonProperty.Access.WRITE_ONLY)
    public void setName(String name) {
        this.title = name;
    }

    public String getArtist() {
        return artist;
    }

    @JsonProperty("imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty(value = "url", access = JsonProperty.Access.WRITE_ONLY)
    public void setUrl(String url) {
        this.imageUrl = url;
    }

    LastFmTrack(String title, String artist, String imageUrl) {
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
    }
}
