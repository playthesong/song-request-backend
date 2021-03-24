package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.domain.song.SongRepository;
import com.requestrealpiano.songrequest.domain.song.searchapi.request.SearchSongParameters;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;
import com.requestrealpiano.songrequest.service.searchapi.SearchApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SongService {

    private final SongRepository songRepository;
    private final SearchApiService searchApiService;

    @Transactional
    public Song updateRequestCountOrElseCreate(SongRequest songRequest) {
        return songRepository.findBySongTitleContainingIgnoreCaseAndArtistIgnoreCase(songRequest.getTitle(), songRequest.getArtist())
                             .map(Song::increaseRequestCount)
                             .orElseGet(() -> {
                                 Song song = Song.from(songRequest);
                                 return songRepository.save(song);
                             });
    }

    public SearchApiResponse searchSong(SearchSongParameters parameters) {
        return searchApiService.requestSearchApiResponse(parameters);
    }
}
