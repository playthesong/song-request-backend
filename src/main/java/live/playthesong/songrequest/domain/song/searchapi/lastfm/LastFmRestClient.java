package live.playthesong.songrequest.domain.song.searchapi.lastfm;

import live.playthesong.songrequest.config.searchapi.LastFmProperties;
import live.playthesong.songrequest.domain.song.searchapi.request.SearchSongParameters;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@Getter
@Component
public class LastFmRestClient {

    private final LastFmProperties lastFmProperties;

    public String searchLastFm(SearchSongParameters parameters) {
        RestTemplate restTemplate = new RestTemplate();
        String checkedArtist = createValidWord(parameters.getArtist());
        String checkedTrack = createValidWord(parameters.getTitle());
        URI requestURI = createLastFmRequestURI(checkedArtist, checkedTrack);
        return restTemplate.getForObject(requestURI, String.class);
    }

    private URI createLastFmRequestURI(String artist, String track) {
        String baseUrl = lastFmProperties.getUrl();
        String method = lastFmProperties.getMethod();
        String limit = lastFmProperties.getLimit();
        String apiKey = lastFmProperties.getApiKey();
        String format = lastFmProperties.getFormat();

        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                                   .queryParam("method", method)
                                   .queryParam("limit", limit)
                                   .queryParam("api_key", apiKey)
                                   .queryParam("format", format)
                                   .queryParam("artist", artist)
                                   .queryParam("track", track)
                                   .build()
                                   .toUri();
    }

    private String createValidWord(String word) {
        if (StringUtils.isEmpty(word)) {
            return "default";
        }
        return word;
    }
}
