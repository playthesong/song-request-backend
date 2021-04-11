package live.playthesong.songrequest.service.searchapi;

import live.playthesong.songrequest.searchapi.lastfm.LastFmRestClient;
import live.playthesong.songrequest.searchapi.request.SearchSongParameters;
import live.playthesong.songrequest.searchapi.response.SearchApiResponse;
import live.playthesong.songrequest.searchapi.translator.JsonTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LastFmApiService implements SearchApiService {

    private final LastFmRestClient lastFmRestClient;
    private final JsonTranslator jsonTranslator;

    @Override
    public SearchApiResponse requestSearchApiResponse(SearchSongParameters parameters) {
        String rawJson = lastFmRestClient.searchLastFm(parameters);
        return jsonTranslator.mapToLastFmResponse(rawJson);
    }
}
