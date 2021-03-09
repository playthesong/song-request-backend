package com.requestrealpiano.songrequest.security.oauth;

import com.requestrealpiano.songrequest.security.jwt.JwtProperties;
import com.requestrealpiano.songrequest.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuthAccount oAuthAccount = (OAuthAccount) authentication.getPrincipal();
        String generationKey = jwtTokenProvider.createGenerationKey(oAuthAccount.getEmail());
        response.sendRedirect(createRedirectURI(generationKey));
    }

    private String createRedirectURI(String generationKey) {
        return UriComponentsBuilder.fromHttpUrl(jwtProperties.getTokenUrl())
                                   .queryParam("key", generationKey)
                                   .build()
                                   .toUri()
                                   .toString();
    }
}
