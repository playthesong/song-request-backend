package com.requestrealpiano.songrequest.service.searchapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.LastFmRestClient;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.translator.JsonTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LastFmApiService implements SearchApiService {

    private final LastFmRestClient lastFmRestClient;
    private final JsonTranslator jsonTranslator;

    // TODO: JsonProcessingException 처리
    @Override
    public SearchApiResponse requestSearchApiResponse(String artist, String title) throws JsonProcessingException {
        String rawJson = lastFmRestClient.searchLastFm(artist, title);
        SearchApiResponse lastFmResponse = jsonTranslator.mapToLastFmResponse(rawJson);
        return lastFmResponse;
    }
}
