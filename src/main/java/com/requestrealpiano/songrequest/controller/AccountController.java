package com.requestrealpiano.songrequest.controller;

import com.requestrealpiano.songrequest.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/auth")
    public void generateToken(@RequestHeader HttpHeaders httpHeaders, HttpServletResponse response) {
        String jwtToken = accountService.generateJwtToken(httpHeaders.getFirst(HttpHeaders.AUTHORIZATION));
        response.addHeader(HttpHeaders.AUTHORIZATION, jwtToken);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/auth/validation")
    public void validateToken(@RequestHeader HttpHeaders httpHeaders) {
        accountService.validateJwtToken(httpHeaders.getFirst(HttpHeaders.AUTHORIZATION));
    }
}
