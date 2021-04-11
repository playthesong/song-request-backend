package live.playthesong.songrequest.service.searchapi;

import live.playthesong.songrequest.searchapi.request.SearchSongParameters;
import live.playthesong.songrequest.searchapi.response.SearchApiResponse;

public interface SearchApiService {

    SearchApiResponse requestSearchApiResponse(SearchSongParameters parameters);
}
