package com.requestrealpiano.songrequest.service.searchapi;

import com.requestrealpiano.songrequest.domain.song.searchapi.request.SearchSongParameters;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;

public interface SearchApiService {

    SearchApiResponse requestSearchApiResponse(SearchSongParameters parameters);
}
