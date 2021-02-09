package com.requestrealpiano.songrequest.service.searchapi;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface SearchApiService<T> {

    T requestSearchApiResponse(String artist, String title) throws JsonProcessingException;
}
