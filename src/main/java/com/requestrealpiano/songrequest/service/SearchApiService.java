package com.requestrealpiano.songrequest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.config.searchapi.LastFmProperties;
import com.requestrealpiano.songrequest.config.searchapi.ManiaDbProperties;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.LastFmRestClient;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.LastFmResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.ManiaDbRestClient;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbClientResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.util.JsonTranslator;
import com.requestrealpiano.songrequest.domain.song.searchapi.util.XmlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SearchApiService {

    private final ManiaDbProperties maniaDbProperties;
    private final LastFmProperties lastFmProperties;
    private final JsonTranslator jsonTranslator;

    // TODO: JsonProcessingException 처리
    public ManiaDbResponse searchManiaDbResponse(String artist, String title) throws JsonProcessingException {
        ManiaDbRestClient maniaDbRestClient = ManiaDbRestClient.of(maniaDbProperties, artist, title);
        String rawXml = maniaDbRestClient.searchManiaDb();
        ManiaDbClientResponse maniaDbData = XmlUtil.mapToManiaDbData(rawXml);
        return ManiaDbResponse.from(maniaDbData);
    }

    // TODO: JsonProcessingException 처리
    public LastFmResponse searchLastFmResponse(String artist, String title) throws JsonProcessingException {
        LastFmRestClient lastFmRestClient = LastFmRestClient.of(lastFmProperties, artist, title);
        String rawJson = lastFmRestClient.searchLastFm();
        LastFmResponse lastFmResponse = jsonTranslator.mapToLastFmResponse(rawJson);
        return lastFmResponse;
    }
}
