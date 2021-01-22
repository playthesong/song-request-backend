package com.requestrealpiano.songrequest.domain.song.searchapi.maniadb;

import com.requestrealpiano.songrequest.config.searchapi.ManiaDbProperties;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Getter
public class ManiaDbRestClient {

    private String baseUrl;
    private String display;
    private String method;
    private String apiKey;
    private String version;
    private String keyword;

    @Builder
    private ManiaDbRestClient(String baseUrl, String display, String method, String apiKey, String version,
                              String keyword) {
        this.baseUrl = baseUrl;
        this.display = display;
        this.method = method;
        this.apiKey = apiKey;
        this.version = version;
        this.keyword = keyword;
    }

    public String searchManiaDbXmlResponse() {
        RestTemplate restTemplate = new RestTemplate();
        URI requestURI = createManiaDbRequestURI();
        return restTemplate.getForObject(requestURI, String.class);
    }

    private URI createManiaDbRequestURI() {
        return UriComponentsBuilder.fromHttpUrl(baseUrl + keyword)
                                   .queryParam("sr", method)
                                   .queryParam("display", display)
                                   .queryParam("key", apiKey)
                                   .queryParam("v", version)
                                   .build()
                                   .toUri();
    }

    public static ManiaDbRestClient of(ManiaDbProperties maniaDbProperties, String artist, String title) {
        String checkedArtist = StringUtils.defaultString(artist);
        String checkedTitle = StringUtils.defaultString(title);

        return ManiaDbRestClient.builder()
                                .baseUrl(maniaDbProperties.getUrl())
                                .display(maniaDbProperties.getDisplay())
                                .method(maniaDbProperties.getMethod())
                                .apiKey(maniaDbProperties.getApiKey())
                                .version(maniaDbProperties.getVersion())
                                .keyword(checkedArtist + checkedTitle)
                                .build();
    }
}
