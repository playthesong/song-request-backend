package live.playthesong.songrequest.service.searchapi;

import live.playthesong.songrequest.domain.song.searchapi.request.SearchSongParameters;
import live.playthesong.songrequest.domain.song.searchapi.response.SearchApiResponse;

public interface SearchApiService {

    SearchApiResponse requestSearchApiResponse(SearchSongParameters parameters);
}
