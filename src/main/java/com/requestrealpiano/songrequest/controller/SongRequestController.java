package com.requestrealpiano.songrequest.controller;

import com.requestrealpiano.songrequest.domain.songrequest.dto.response.SongRequestsResponse;
import com.requestrealpiano.songrequest.service.SongRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/songrequests")
public class SongRequestController {

    private final SongRequestService songRequestService;

    @GetMapping("")
    public ResponseEntity<SongRequestsResponse> findAll() {
        return new ResponseEntity(songRequestService.findAllSongRequests(), HttpStatus.OK);
    }

}
