package com.requestrealpiano.songrequest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;
import com.requestrealpiano.songrequest.global.response.ApiResponse;
import com.requestrealpiano.songrequest.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.requestrealpiano.songrequest.global.response.ApiResponse.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/songs")
public class SongController {

    private final SongService songService;

    @GetMapping
    public ApiResponse<SearchApiResponse> search(@RequestParam String artist,
                                                 @RequestParam String title) throws JsonProcessingException {
        SearchApiResponse searchApiResponse = songService.searchSong(artist, title);
        return OK(searchApiResponse);
    }
}
