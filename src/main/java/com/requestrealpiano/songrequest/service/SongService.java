package com.requestrealpiano.songrequest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SongService {

    private final SearchApiService searchApiService;

    public ManiaDbResponse searchManiaDb(String artist, String title) throws JsonProcessingException {
        return searchApiService.searchManiaDbResponse(artist, title);
    }
}
