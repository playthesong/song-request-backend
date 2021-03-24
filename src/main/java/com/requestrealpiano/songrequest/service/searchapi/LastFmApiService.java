package com.requestrealpiano.songrequest.service.searchapi;

import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.LastFmRestClient;
import com.requestrealpiano.songrequest.domain.song.searchapi.request.SearchSongParameters;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.translator.JsonTranslator;
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
