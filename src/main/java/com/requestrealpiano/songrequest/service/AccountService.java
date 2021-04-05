package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountDetail;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.domain.letter.LetterRepository;
import com.requestrealpiano.songrequest.global.error.exception.business.AccountNotFoundException;
import com.requestrealpiano.songrequest.security.jwt.JwtTokenProvider;
import com.requestrealpiano.songrequest.security.oauth.OAuthAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final LetterRepository letterRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String generateJwtToken(String generationKey) {
        String email = jwtTokenProvider.parseGenerationKey(generationKey);
        Account account = accountRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);
        return jwtTokenProvider.createJwtToken(account);
    }

    public void validateJwtToken(String jwtToken) {
        jwtTokenProvider.validateJwtToken(jwtToken);
    }

    public AccountDetail findAccountDetail(OAuthAccount loginAccount) {
        Account account = accountRepository.findById(loginAccount.getId()).orElseThrow(AccountNotFoundException::new);
        return AccountDetail.from(account);
    }

    @Transactional
    public void deleteAccount(OAuthAccount loginAccount) {
        Account account = accountRepository.findById(loginAccount.getId()).orElseThrow(AccountNotFoundException::new);
        letterRepository.deleteByAccount(account);
        accountRepository.delete(account);
    }
}
