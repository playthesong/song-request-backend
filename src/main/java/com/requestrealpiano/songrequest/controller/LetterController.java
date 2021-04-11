package com.requestrealpiano.songrequest.controller;

import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import com.requestrealpiano.songrequest.domain.letter.request.DateParameters;
import com.requestrealpiano.songrequest.domain.letter.request.LetterRequest;
import com.requestrealpiano.songrequest.domain.letter.request.PaginationParameters;
import com.requestrealpiano.songrequest.domain.letter.request.StatusChangeRequest;
import com.requestrealpiano.songrequest.domain.letter.response.LettersResponse;
import com.requestrealpiano.songrequest.domain.letter.response.inner.LetterDetails;
import com.requestrealpiano.songrequest.global.response.ApiResponse;
import com.requestrealpiano.songrequest.security.oauth.LoginAccount;
import com.requestrealpiano.songrequest.security.oauth.OAuthAccount;
import com.requestrealpiano.songrequest.service.LetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ApiResponse<LettersResponse> findAll(@ModelAttribute @Valid PaginationParameters paginationParameters) {
        LettersResponse letters = letterService.findAllLetters(paginationParameters);
        return SUCCESS(OK, letters);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<LetterDetails> create(@LoginAccount OAuthAccount loginAccount,
                                             @RequestBody @Valid LetterRequest letterRequest) {
        LetterDetails newLetter = letterService.createLetter(loginAccount, letterRequest);
        return SUCCESS(CREATED, newLetter);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ApiResponse<LetterDetails> findById(@PathVariable Long id) {
        LetterDetails letter = letterService.findLetter(id);
        return SUCCESS(OK, letter);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public ApiResponse<LetterDetails> updateById(@LoginAccount OAuthAccount loginAccount,
                                                 @PathVariable Long id, @RequestBody @Valid LetterRequest letterRequest) {
        LetterDetails letter = letterService.updateLetter(loginAccount, id, letterRequest);
        return SUCCESS(OK, letter);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@LoginAccount OAuthAccount loginAccount, @PathVariable Long id) {
        letterService.deleteLetter(loginAccount, id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/status")
    public ApiResponse<LetterDetails> changeStatus(@PathVariable Long id,
                                                   @RequestBody @Valid StatusChangeRequest statusChangeRequest) {
        LetterDetails letter = letterService.changeStatus(id, statusChangeRequest);
        return SUCCESS(OK, letter);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/status/{requestStatus}")
    public ApiResponse<LettersResponse> findByStatus(@PathVariable RequestStatus requestStatus,
                                                     @ModelAttribute @Valid PaginationParameters paginationParameters,
                                                     @ModelAttribute DateParameters dateParameters) {
        LettersResponse letters = letterService.findLettersByStatus(requestStatus, paginationParameters, dateParameters);
        return SUCCESS(OK, letters);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/yesterday")
    public void initialize() {
        letterService.initializeTodayLetters();
    }
}
