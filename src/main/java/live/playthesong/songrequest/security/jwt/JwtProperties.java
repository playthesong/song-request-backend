package live.playthesong.songrequest.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@RequiredArgsConstructor
@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private final String tokenSecret;
    private final String tokenExpiration;
    private final String header;
    private final String headerPrefix;
    private final String tokenUrl;
    private final String generationKeySecret;
    private final String generationKeyExpiration;
}
