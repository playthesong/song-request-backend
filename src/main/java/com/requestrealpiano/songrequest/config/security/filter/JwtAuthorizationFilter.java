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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Getter
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Builder
    private JwtAuthorizationFilter(AuthenticationManager authenticationManager, AccountRepository accountRepository,
                                   JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.accountRepository = accountRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        AccountClaims accountClaims = jwtTokenProvider.parseJwtToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        Account account = accountRepository.findByEmail(accountClaims.getEmail()).orElseThrow(AccountNotFoundException::new);
        OAuthAccount oAuthAccount = OAuthAccount.from(account);
        Authentication authentication = new UsernamePasswordAuthenticationToken(oAuthAccount, null, oAuthAccount.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    public static JwtAuthorizationFilter of(AuthenticationManager authenticationManager, AccountRepository accountRepository,
                                            JwtTokenProvider jwtTokenProvider) {
        return JwtAuthorizationFilter.builder()
                                     .authenticationManager(authenticationManager)
                                     .accountRepository(accountRepository)
                                     .jwtTokenProvider(jwtTokenProvider)
                                     .build();
    }
}
