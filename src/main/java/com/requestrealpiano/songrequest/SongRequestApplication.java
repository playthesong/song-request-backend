package com.requestrealpiano.songrequest;

import com.requestrealpiano.songrequest.config.searchapi.LastFmProperties;
import com.requestrealpiano.songrequest.config.searchapi.ManiaDbProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(value = {ManiaDbProperties.class, LastFmProperties.class})
@SpringBootApplication
public class SongRequestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SongRequestApplication.class, args);
    }
}
