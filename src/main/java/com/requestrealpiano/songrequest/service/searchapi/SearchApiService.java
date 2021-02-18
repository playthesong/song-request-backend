package com.requestrealpiano.songrequest.service.searchapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;

public interface SearchApiService {

    SearchApiResponse requestSearchApiResponse(String artist, String title);
}
