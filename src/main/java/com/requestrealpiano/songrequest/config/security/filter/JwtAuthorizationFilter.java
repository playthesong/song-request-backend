package com.requestrealpiano.songrequest.config.security.filter;

import com.requestrealpiano.songrequest.config.security.jwt.JwtTokenProvider;
import com.requestrealpiano.songrequest.config.security.jwt.claims.AccountClaims;
import com.requestrealpiano.songrequest.config.security.oauth.OAuthAccount;
import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.global.error.exception.business.AccountNotFoundException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Getter
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Builder
    private JwtAuthorizationFilter(AccountRepository accountRepository,
                                   JwtTokenProvider jwtTokenProvider) {
        this.accountRepository = accountRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtToken = jwtTokenProvider.extractToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        AccountClaims accountClaims = jwtTokenProvider.parseJwtToken(jwtToken);
        Account account = accountRepository.findByEmail(accountClaims.getEmail()).orElseThrow(AccountNotFoundException::new);
        OAuthAccount oAuthAccount = OAuthAccount.from(account);
        Authentication authentication = new UsernamePasswordAuthenticationToken(oAuthAccount, jwtToken, oAuthAccount.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    public static JwtAuthorizationFilter of(AccountRepository accountRepository, JwtTokenProvider jwtTokenProvider) {
        return JwtAuthorizationFilter.builder()
                                     .accountRepository(accountRepository)
                                     .jwtTokenProvider(jwtTokenProvider)
                                     .build();
    }
}
