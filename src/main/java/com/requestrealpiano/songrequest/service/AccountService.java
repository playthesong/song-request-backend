package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.config.security.jwt.JwtTokenProvider;
import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.global.error.exception.JwtValidationException;
import com.requestrealpiano.songrequest.global.error.exception.business.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String generateJwtToken(String generationKey) {
        if (StringUtils.isEmpty(generationKey)) {
            throw new JwtValidationException();
        }

        String email = jwtTokenProvider.parseGenerationKey(generationKey);
        Account account = accountRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);

        return jwtTokenProvider.createJwtToken(account);
    }
}
