package live.playthesong.songrequest.service.searchapi;

import live.playthesong.songrequest.searchapi.maniadb.ManiaDbRestClient;
import live.playthesong.songrequest.searchapi.maniadb.response.ManiaDbClientResponse;
import live.playthesong.songrequest.searchapi.request.SearchSongParameters;
import live.playthesong.songrequest.searchapi.response.SearchApiResponse;
import live.playthesong.songrequest.searchapi.translator.XmlTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Primary
public class ManiaDbApiService implements SearchApiService {

    private final ManiaDbRestClient maniaDbRestClient;
    private final XmlTranslator xmlTranslator;

    @Override
    public SearchApiResponse requestSearchApiResponse(SearchSongParameters parameters) {
        String rawXml = maniaDbRestClient.searchManiaDb(parameters);
        ManiaDbClientResponse maniaDbData = xmlTranslator.mapToManiaDbData(rawXml);
        return SearchApiResponse.from(maniaDbData);
    }
}
