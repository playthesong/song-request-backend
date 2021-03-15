package com.requestrealpiano.songrequest.controller;

import com.requestrealpiano.songrequest.domain.letter.request.NewLetterRequest;
import com.requestrealpiano.songrequest.domain.letter.response.LetterResponse;
import com.requestrealpiano.songrequest.global.response.ApiResponse;
import com.requestrealpiano.songrequest.global.response.StatusCode;
import com.requestrealpiano.songrequest.service.LetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.requestrealpiano.songrequest.global.response.ApiResponse.SUCCESS;
import static com.requestrealpiano.songrequest.global.response.StatusCode.CREATED;
import static com.requestrealpiano.songrequest.global.response.StatusCode.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/letters")
public class LetterController {

    private final LetterService letterService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ApiResponse<List<LetterResponse>> findAll() {
        List<LetterResponse> letters = letterService.findAllLetters();
        return SUCCESS(OK, letters);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ApiResponse<LetterResponse> findById(@PathVariable Long id) {
        LetterResponse letter = letterService.findLetter(id);
        return SUCCESS(OK, letter);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<LetterResponse> createNew(@RequestBody @Valid NewLetterRequest newLetterRequest) {
        LetterResponse newLetter = letterService.createNewLetter(newLetterRequest);
        return SUCCESS(CREATED, newLetter);
    }
}
