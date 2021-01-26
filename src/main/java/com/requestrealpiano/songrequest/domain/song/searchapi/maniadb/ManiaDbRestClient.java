package com.requestrealpiano.songrequest.domain.song.searchapi.maniadb;

import com.requestrealpiano.songrequest.config.searchapi.ManiaDbProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@Getter
@Component
public class ManiaDbRestClient {

    private final ManiaDbProperties maniaDbProperties;

    public String searchManiaDb(String artist, String title) {
        RestTemplate restTemplate = new RestTemplate();
        String keyword = createValidKeyword(artist, title);
        URI requestURI = createManiaDbRequestURI(keyword);
        return restTemplate.getForObject(requestURI, String.class);
    }

    private URI createManiaDbRequestURI(String keyword) {
        String baseUrl = maniaDbProperties.getUrl();
        String method = maniaDbProperties.getMethod();
        String display = maniaDbProperties.getDisplay();
        String apiKey = maniaDbProperties.getApiKey();
        String version = maniaDbProperties.getVersion();

        return UriComponentsBuilder.fromHttpUrl(baseUrl + keyword)
                                   .queryParam("sr", method)
                                   .queryParam("display", display)
                                   .queryParam("key", apiKey)
                                   .queryParam("v", version)
                                   .build()
                                   .toUri();
    }

    private String createValidKeyword(String artist, String title) {
        String checkedArtist = artist;
        String checkedTitle = title;
        if (StringUtils.isEmpty(artist)) {
            checkedArtist = "Artist";
        }

        if (StringUtils.isEmpty(title)) {
            checkedTitle = "Title";
        }
        return checkedArtist + checkedTitle;
    }
}
