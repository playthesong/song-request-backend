package live.playthesong.songrequest.domain.song.response;

import live.playthesong.songrequest.domain.song.Song;
import live.playthesong.songrequest.domain.song.response.inner.SongDetails;
import live.playthesong.songrequest.global.pagination.response.PageDetails;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SongRankingResponse {

    private final PageDetails pageDetails;
    private final List<SongDetails> songs;

    @Builder
    private SongRankingResponse(PageDetails pageDetails, List<SongDetails> songs) {
        this.pageDetails = pageDetails;
        this.songs = songs;
    }

    public static SongRankingResponse from(Page<Song> currentPageSongs) {
        List<SongDetails> songs = currentPageSongs.getContent()
                                                  .stream()
                                                  .map(SongDetails::from)
                                                  .collect(Collectors.toList());

        return SongRankingResponse.builder()
                                  .pageDetails(PageDetails.from(currentPageSongs))
                                  .songs(songs)
                                  .build();
    }
}
