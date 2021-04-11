package live.playthesong.songrequest.service;

import live.playthesong.songrequest.domain.account.Account;
import live.playthesong.songrequest.domain.account.AccountDetail;
import live.playthesong.songrequest.domain.account.AccountRepository;
import live.playthesong.songrequest.domain.letter.LetterRepository;
import live.playthesong.songrequest.global.error.exception.business.AccountNotFoundException;
import live.playthesong.songrequest.security.jwt.JwtTokenProvider;
import live.playthesong.songrequest.security.oauth.OAuthAccount;
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
