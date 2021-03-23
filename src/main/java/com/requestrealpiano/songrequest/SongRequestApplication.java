package com.requestrealpiano.songrequest;

import com.requestrealpiano.songrequest.config.searchapi.LastFmProperties;
import com.requestrealpiano.songrequest.config.searchapi.ManiaDbProperties;
import com.requestrealpiano.songrequest.security.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

import static com.requestrealpiano.songrequest.global.constant.JpaProperties.Seoul;

@EnableConfigurationProperties(value = {JwtProperties.class, ManiaDbProperties.class, LastFmProperties.class})
@SpringBootApplication
public class SongRequestApplication {

    @PostConstruct
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(Seoul));
    }

    public static void main(String[] args) {
        SpringApplication.run(SongRequestApplication.class, args);
    }
}
