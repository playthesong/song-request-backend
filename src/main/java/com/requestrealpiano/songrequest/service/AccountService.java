package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.global.error.exception.business.AccountNotFoundException;
import com.requestrealpiano.songrequest.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String generateJwtToken(String generationKey) {
        String email = jwtTokenProvider.parseGenerationKey(generationKey);
        Account account = accountRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);
        return jwtTokenProvider.createJwtToken(account);
    }

    public void validateJwtToken(String jwtToken) {
        jwtTokenProvider.validateJwtToken(jwtToken);
    }
}
