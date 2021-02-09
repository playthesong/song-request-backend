package com.requestrealpiano.songrequest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;
import com.requestrealpiano.songrequest.service.searchapi.SearchApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SongService {

    private final SearchApiService searchApiService;

    public SearchApiResponse searchSong(String artist, String title) throws JsonProcessingException {
        return searchApiService.requestSearchApiResponse(artist, title);
    }
}
