package com.requestrealpiano.songrequest.controller;

import com.requestrealpiano.songrequest.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/token")
    public ResponseEntity<Void> generateToken(@RequestHeader HttpHeaders httpHeaders) {
        String jwtToken = accountService.generateJwtToken(httpHeaders.getFirst(HttpHeaders.AUTHORIZATION));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.AUTHORIZATION, jwtToken);
        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }
}
