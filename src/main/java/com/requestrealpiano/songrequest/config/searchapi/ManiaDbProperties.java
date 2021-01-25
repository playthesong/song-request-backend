package com.requestrealpiano.songrequest.config.searchapi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "maniadb")
public class ManiaDbProperties {

    private String url;
    private String display;
    private String method;
    private String apiKey;
    private String version;
}
