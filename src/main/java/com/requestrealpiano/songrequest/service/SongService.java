package com.requestrealpiano.songrequest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.domain.song.SongRepository;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;
import com.requestrealpiano.songrequest.service.searchapi.SearchApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SongService {

    private final SongRepository songRepository;
    private final SearchApiService searchApiService;

    public Song findSongByRequest(SongRequest songRequest) {
        return songRepository.findBySongTitleContainingIgnoreCaseAndArtistIgnoreCase(songRequest.getTitle(), songRequest.getArtist())
                             .map(Song::increaseRequestCount)
                             .orElseGet(() -> {
                                 Song song = Song.from(songRequest);
                                 return songRepository.save(song);
                             });
    }

    public SearchApiResponse searchSong(String artist, String title) throws JsonProcessingException {
        return searchApiService.requestSearchApiResponse(artist, title);
    }
}
