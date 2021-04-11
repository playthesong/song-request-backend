package live.playthesong.songrequest.service;

import live.playthesong.songrequest.domain.letter.request.inner.SongRequest;
import live.playthesong.songrequest.domain.song.Song;
import live.playthesong.songrequest.domain.song.SongRepository;
import live.playthesong.songrequest.domain.song.searchapi.request.SearchSongParameters;
import live.playthesong.songrequest.domain.song.searchapi.response.SearchApiResponse;
import live.playthesong.songrequest.service.searchapi.SearchApiService;
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
        return songRepository.findBySongTitleAndArtist(songRequest.getTitle(), songRequest.getArtist())
                             .stream().findFirst()
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
