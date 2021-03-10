package com.requestrealpiano.songrequest.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.global.error.exception.JwtValidationException;
import com.requestrealpiano.songrequest.global.error.exception.business.AccountNotFoundException;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import com.requestrealpiano.songrequest.global.error.response.ErrorResponse;
import com.requestrealpiano.songrequest.security.jwt.JwtTokenProvider;
import com.requestrealpiano.songrequest.security.jwt.claims.AccountClaims;
import com.requestrealpiano.songrequest.security.oauth.OAuthAccount;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Getter
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Builder
    private JwtAuthorizationFilter(AccountRepository accountRepository, JwtTokenProvider jwtTokenProvider,
                                   ObjectMapper objectMapper) {
        this.accountRepository = accountRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String jwtToken = jwtTokenProvider.extractToken(request.getHeader(HttpHeaders.AUTHORIZATION));
            AccountClaims accountClaims = jwtTokenProvider.parseJwtToken(jwtToken);
            Account account = accountRepository.findByEmail(accountClaims.getEmail()).orElseThrow(AccountNotFoundException::new);
            OAuthAccount oAuthAccount = OAuthAccount.from(account);
            Authentication authentication = new UsernamePasswordAuthenticationToken(oAuthAccount, jwtToken, oAuthAccount.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (JwtValidationException exception) {
            log.error("handleAuthenticationFilterJwtException", exception);
            ErrorCode errorCode = exception.getErrorCode();
            handleAuthenticationFilterException(response, errorCode);
        } catch (Exception exception) {
            log.error("handleAuthenticationFilterException", exception);
            ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
            handleAuthenticationFilterException(response, errorCode);
        }
    }

    private void handleAuthenticationFilterException(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        String errorResponse = objectMapper.writeValueAsString(ErrorResponse.from(errorCode));
        response.getWriter().print(errorResponse);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorCode.getStatusCode());
        response.getWriter().flush();
        response.getWriter().close();
    }

    public static JwtAuthorizationFilter of(AccountRepository accountRepository, JwtTokenProvider jwtTokenProvider,
                                            ObjectMapper objectMapper) {
        return JwtAuthorizationFilter.builder()
                                     .accountRepository(accountRepository)
                                     .jwtTokenProvider(jwtTokenProvider)
                                     .objectMapper(objectMapper)
                                     .build();
    }
}
