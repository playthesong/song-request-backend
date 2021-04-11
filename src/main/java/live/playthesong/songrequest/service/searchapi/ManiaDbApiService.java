package live.playthesong.songrequest.service.searchapi;

import live.playthesong.songrequest.domain.song.searchapi.maniadb.ManiaDbRestClient;
import live.playthesong.songrequest.domain.song.searchapi.maniadb.response.ManiaDbClientResponse;
import live.playthesong.songrequest.domain.song.searchapi.request.SearchSongParameters;
import live.playthesong.songrequest.domain.song.searchapi.response.SearchApiResponse;
import live.playthesong.songrequest.domain.song.searchapi.translator.XmlTranslator;
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
