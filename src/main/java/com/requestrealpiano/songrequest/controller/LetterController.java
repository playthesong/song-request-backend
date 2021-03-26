package com.requestrealpiano.songrequest.controller;

import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import com.requestrealpiano.songrequest.domain.letter.request.NewLetterRequest;
import com.requestrealpiano.songrequest.domain.letter.response.LetterResponse;
import com.requestrealpiano.songrequest.global.response.ApiResponse;
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<LetterResponse> create(@RequestBody @Valid NewLetterRequest newLetterRequest) {
        LetterResponse newLetter = letterService.createLetter(newLetterRequest);
        return SUCCESS(CREATED, newLetter);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ApiResponse<LetterResponse> findById(@PathVariable Long id) {
        LetterResponse letter = letterService.findLetter(id);
        return SUCCESS(OK, letter);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/status/{requestStatus}")
    public ApiResponse<List<LetterResponse>> findByStatus(@PathVariable RequestStatus requestStatus) {
        return null;
    }
}
