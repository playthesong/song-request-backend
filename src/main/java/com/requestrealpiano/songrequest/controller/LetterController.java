package com.requestrealpiano.songrequest.controller;

import com.requestrealpiano.songrequest.domain.letter.dto.response.LetterResponse;
import com.requestrealpiano.songrequest.service.LetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/letters")
public class LetterController {

    private final LetterService letterService;

    @GetMapping("")
    public ResponseEntity<LetterResponse> findAll() {
        return new ResponseEntity(letterService.findAllLetters(), HttpStatus.OK);
    }

}
