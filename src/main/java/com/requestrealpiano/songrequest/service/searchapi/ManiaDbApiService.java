package com.requestrealpiano.songrequest.service.searchapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.ManiaDbRestClient;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbClientResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.translator.XmlTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Primary
public class ManiaDbApiService implements SearchApiService {

    private final ManiaDbRestClient maniaDbRestClient;
    private final XmlTranslator xmlTranslator;

    // TODO: JsonProcessingException 처리
    @Override
    public SearchApiResponse requestSearchApiResponse(String artist, String title) throws JsonProcessingException {
        String rawXml = maniaDbRestClient.searchManiaDb(artist, title);
        ManiaDbClientResponse maniaDbData = xmlTranslator.mapToManiaDbData(rawXml);
        return SearchApiResponse.from(maniaDbData);
    }
}
