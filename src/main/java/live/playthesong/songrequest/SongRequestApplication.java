package live.playthesong.songrequest;

import live.playthesong.songrequest.config.searchapi.LastFmProperties;
import live.playthesong.songrequest.config.searchapi.ManiaDbProperties;
import live.playthesong.songrequest.security.jwt.JwtProperties;
import live.playthesong.songrequest.global.constant.JpaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableConfigurationProperties(value = {JwtProperties.class, ManiaDbProperties.class, LastFmProperties.class})
@SpringBootApplication
public class SongRequestApplication {

    @PostConstruct
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(JpaProperties.Seoul));
    }

    public static void main(String[] args) {
        SpringApplication.run(SongRequestApplication.class, args);
    }
}
