package com.requestrealpiano.songrequest.service.searchapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.ManiaDbRestClient;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbClientResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.translator.XmlTranslator;
import com.requestrealpiano.songrequest.global.annotation.ManiaDb;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@ManiaDb
@Service
public class ManiaDbApiService implements SearchApiService<ManiaDbResponse> {

    private final ManiaDbRestClient maniaDbRestClient;
    private final XmlTranslator xmlTranslator;

    // TODO: JsonProcessingException 처리
    @Override
    public ManiaDbResponse requestSearchApiResponse(String artist, String title) throws JsonProcessingException {
        String rawXml = maniaDbRestClient.searchManiaDb(artist, title);
        ManiaDbClientResponse maniaDbData = xmlTranslator.mapToManiaDbData(rawXml);
        return ManiaDbResponse.from(maniaDbData);
    }
}
