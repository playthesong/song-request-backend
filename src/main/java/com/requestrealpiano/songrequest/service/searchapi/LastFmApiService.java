package com.requestrealpiano.songrequest.service.searchapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.LastFmRestClient;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.LastFmResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.translator.JsonTranslator;
import com.requestrealpiano.songrequest.global.annotation.LastFm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@LastFm
@Service
public class LastFmApiService implements SearchApiService<LastFmResponse> {

    private final LastFmRestClient lastFmRestClient;
    private final JsonTranslator jsonTranslator;

    // TODO: JsonProcessingException 처리
    @Override
    public LastFmResponse requestSearchApiResponse(String artist, String title) throws JsonProcessingException {
        String rawJson = lastFmRestClient.searchLastFm(artist, title);
        LastFmResponse lastFmResponse = jsonTranslator.mapToLastFmResponse(rawJson);
        return lastFmResponse;
    }
}
