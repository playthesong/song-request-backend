package com.requestrealpiano.songrequest.config.searchapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "maniadb")
public class ManiaDbProperties {

    private final String url;
    private final String display;
    private final String method;
    private final String apiKey;
    private final String version;
}
