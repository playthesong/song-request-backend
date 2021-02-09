package com.requestrealpiano.songrequest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.LastFmResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbResponse;
import com.requestrealpiano.songrequest.global.annotation.ManiaDb;
import com.requestrealpiano.songrequest.service.searchapi.SearchApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SongService {

    @ManiaDb
    private final SearchApiService<ManiaDbResponse> searchApiService;

    public ManiaDbResponse searchSong(String artist, String title) throws JsonProcessingException {
        return searchApiService.requestSearchApiResponse(artist, title);
    }
}
