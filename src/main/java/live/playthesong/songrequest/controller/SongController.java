package live.playthesong.songrequest.controller;

import live.playthesong.songrequest.domain.letter.request.PaginationParameters;
import live.playthesong.songrequest.domain.song.response.SongRankingResponse;
import live.playthesong.songrequest.searchapi.request.SearchSongParameters;
import live.playthesong.songrequest.searchapi.response.SearchApiResponse;
import live.playthesong.songrequest.global.response.ApiResponse;
import live.playthesong.songrequest.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static live.playthesong.songrequest.global.response.ApiResponse.SUCCESS;
import static live.playthesong.songrequest.global.response.StatusCode.OK;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/songs")
public class SongController {

    private final SongService songService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ranking")
    public ApiResponse<SongRankingResponse> ranking(@ModelAttribute @Valid PaginationParameters parameters) {
        SongRankingResponse rankingResponse = songService.findSongs(parameters);
        return SUCCESS(OK, rankingResponse);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ApiResponse<SearchApiResponse> search(@ModelAttribute @Valid SearchSongParameters parameters) {
        SearchApiResponse searchApiResponse = songService.searchSong(parameters);
        return SUCCESS(OK, searchApiResponse);
    }
}
