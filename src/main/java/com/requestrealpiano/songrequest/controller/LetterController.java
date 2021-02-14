package com.requestrealpiano.songrequest.controller;

import com.requestrealpiano.songrequest.domain.letter.dto.response.LetterResponse;
import com.requestrealpiano.songrequest.global.response.ApiResponse;
import com.requestrealpiano.songrequest.service.LetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.requestrealpiano.songrequest.global.response.ApiResponse.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/letters")
public class LetterController {

    private final LetterService letterService;

    @GetMapping
    public ApiResponse<List<LetterResponse>> findAll() {
        List<LetterResponse> letters = letterService.findAllLetters();
        return OK(letters);
    }

    @GetMapping("/{id}")
    public ApiResponse<LetterResponse> findById(@PathVariable Long id) {
        LetterResponse letter = letterService.findLetter(id);
        return OK(letter);
    }
}
