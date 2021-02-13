package com.requestrealpiano.songrequest;

import com.requestrealpiano.songrequest.config.searchapi.LastFmProperties;
import com.requestrealpiano.songrequest.config.searchapi.ManiaDbProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableConfigurationProperties(value = {ManiaDbProperties.class, LastFmProperties.class})
@SpringBootApplication
public class SongRequestApplication {

    @PostConstruct
    public void setTimeZone() {
        String SEOUL = "Asia/Seoul";
        TimeZone.setDefault(TimeZone.getTimeZone(SEOUL));
    }

    public static void main(String[] args) {
        SpringApplication.run(SongRequestApplication.class, args);
    }
}
