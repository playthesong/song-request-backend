package com.requestrealpiano.songrequest.domain.song.searchapi.lastfm;

import com.requestrealpiano.songrequest.config.searchapi.LastFmProperties;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Getter
public class LastFmRestClient {

    private final String baseUrl;
    private final String method;
    private final String limit;
    private final String apiKey;
    private final String format;
    private final String artist;
    private final String track;

    @Builder
    private LastFmRestClient(String baseUrl, String method, String limit, String apiKey, String format,
                             String artist, String track) {
        this.baseUrl = baseUrl;
        this.method = method;
        this.limit = limit;
        this.apiKey = apiKey;
        this.format = format;
        this.artist = artist;
        this.track = track;
    }

    public String searchLastFm() {
        RestTemplate restTemplate = new RestTemplate();
        URI requestURI = createLastFmRequestURI();
        return restTemplate.getForObject(requestURI, String.class);
    }

    private URI createLastFmRequestURI() {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                                   .queryParam("method", method)
                                   .queryParam("limit", limit)
                                   .queryParam("api_key", apiKey)
                                   .queryParam("format", format)
                                   .queryParam("artist", artist)
                                   .queryParam("track", track)
                                   .build()
                                   .toUri();
    }

    public static LastFmRestClient of(LastFmProperties lastFmProperties, String artist, String track) {
        String checkedArtist = StringUtils.defaultString(artist);
        String checkedTrack = StringUtils.defaultString(track);

        return LastFmRestClient.builder()
                               .baseUrl(lastFmProperties.getUrl())
                               .method(lastFmProperties.getMethod())
                               .limit(lastFmProperties.getLimit())
                               .apiKey(lastFmProperties.getApiKey())
                               .format(lastFmProperties.getFormat())
                               .artist(checkedArtist)
                               .track(checkedTrack)
                               .build();
    }
}
