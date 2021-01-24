package com.requestrealpiano.songrequest.config.searchapi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix="lastfm")
public class LastFmProperties {

    private String url;
    private String method;
    private String limit;
    private String apiKey;
    private String format;
}
