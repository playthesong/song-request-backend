package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.songrequest.SongRequest;
import com.requestrealpiano.songrequest.domain.songrequest.SongRequestRepository;
import com.requestrealpiano.songrequest.domain.songrequest.dto.response.SongRequestsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SongRequestService {

    private final SongRequestRepository songRequestRepository;

    public List<SongRequestsResponse> findAllSongRequests() {
        List<SongRequest> songRequests = songRequestRepository.findAll();
        return songRequests.stream()
                           .map(SongRequestsResponse::from)
                           .collect(Collectors.toList());
    }
}
