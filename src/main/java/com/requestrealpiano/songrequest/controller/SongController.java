package com.requestrealpiano.songrequest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.LastFmResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbResponse;
import com.requestrealpiano.songrequest.global.response.ApiResponse;
import com.requestrealpiano.songrequest.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/songs")
public class SongController {

    private final SongService songService;

    @GetMapping("/search-api/k-pop")
    public ApiResponse<ManiaDbResponse> searchManiaDbApi(@RequestParam String artist,
                                                         @RequestParam String title) throws JsonProcessingException {
        ManiaDbResponse maniaDbResponse = songService.searchManiaDb(artist, title);
        return ApiResponse.OK(maniaDbResponse);
    }

    @GetMapping("/search-api/pop")
    public ApiResponse<LastFmResponse> searchLastFmApi(@RequestParam String artist,
                                                       @RequestParam String title) throws JsonProcessingException {
        LastFmResponse lastFmResponse = songService.searchLastFm(artist, title);
        return ApiResponse.OK(lastFmResponse);
    }
}
