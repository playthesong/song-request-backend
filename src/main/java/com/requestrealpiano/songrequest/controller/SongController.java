package com.requestrealpiano.songrequest.controller;

import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;
import com.requestrealpiano.songrequest.global.response.ApiResponse;
import com.requestrealpiano.songrequest.global.response.StatusCode;
import com.requestrealpiano.songrequest.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;

import static com.requestrealpiano.songrequest.global.constant.ValidationCondition.*;
import static com.requestrealpiano.songrequest.global.response.ApiResponse.SUCCESS;
import static com.requestrealpiano.songrequest.global.response.StatusCode.OK;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/songs")
public class SongController {

    private final SongService songService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ApiResponse<SearchApiResponse> search(@RequestParam(defaultValue = "artist") @Size(max = ARTIST_MAX)
                                                 String artist,
                                                 @RequestParam(defaultValue = "title") @Size(max = TITLE_MAX)
                                                 String title) {
        SearchApiResponse searchApiResponse = songService.searchSong(artist, title);
        return SUCCESS(OK, searchApiResponse);
    }
}
