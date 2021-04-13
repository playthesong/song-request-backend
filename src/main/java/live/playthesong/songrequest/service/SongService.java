package live.playthesong.songrequest.service;

import live.playthesong.songrequest.domain.letter.request.PaginationParameters;
import live.playthesong.songrequest.domain.letter.request.inner.SongRequest;
import live.playthesong.songrequest.domain.song.Song;
import live.playthesong.songrequest.domain.song.SongRepository;
import live.playthesong.songrequest.domain.song.response.SongRankingResponse;
import live.playthesong.songrequest.global.constant.SortProperties;
import live.playthesong.songrequest.global.pagination.Pagination;
import live.playthesong.songrequest.searchapi.request.SearchSongParameters;
import live.playthesong.songrequest.searchapi.response.SearchApiResponse;
import live.playthesong.songrequest.service.searchapi.SearchApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public SongRankingResponse findSongs(PaginationParameters parameters) {
        PageRequest songPage = Pagination.of(parameters.getPage(), parameters.getSize(), parameters.getDirection(),
                                             SortProperties.REQUEST_COUNT);
        Page<Song> songs = songRepository.findAll(songPage);
        return SongRankingResponse.from(songs);
    }

    public SearchApiResponse searchSong(SearchSongParameters parameters) {
        return searchApiService.requestSearchApiResponse(parameters);
    }
}
