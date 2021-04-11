package live.playthesong.songrequest.config.searchapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@RequiredArgsConstructor
@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "lastfm")
public class LastFmProperties {

    private final String url;
    private final String method;
    private final String limit;
    private final String apiKey;
    private final String format;
}
