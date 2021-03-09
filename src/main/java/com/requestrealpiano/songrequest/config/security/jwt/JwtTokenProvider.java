package com.requestrealpiano.songrequest.config.security.jwt;

import com.requestrealpiano.songrequest.config.security.jwt.claims.AccountClaims;
import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.global.error.exception.JwtValidationException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final String ID = "id";
    private final String EMAIL = "email";
    private final String NAME = "name";
    private final String AVATAR_URL = "avatarUrl";
    private final String ROLE = "role";

    public String createGenerationKey(String email) {
        Date now = new Date();
        return Jwts.builder()
                   .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                   .setIssuedAt(now)
                   .setExpiration(new Date(now.getTime() + Long.parseLong(jwtProperties.getGenerationKeyExpiration())))
                   .claim(EMAIL, email)
                   .signWith(SignatureAlgorithm.HS256, jwtProperties.getGenerationKeySecret())
                   .compact();
    }

    public String parseGenerationKey(String authorization) {
        String generationKey = extractToken(authorization);
        try {
            Jws<Claims> claims = Jwts.parser()
                                     .setSigningKey(jwtProperties.getGenerationKeySecret())
                                     .parseClaimsJws(generationKey);
            return claims.getBody()
                         .get(EMAIL, String.class);
        } catch (JwtException exception) {
            throw new JwtValidationException();
        }
    }

    public String createJwtToken(Account account) {
        Date now = new Date();
        String jwtToken = Jwts.builder()
                              .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                              .setIssuedAt(now)
                              .setExpiration(new Date(now.getTime() + Long.parseLong(jwtProperties.getTokenExpiration())))
                              .claim(ID, account.getId())
                              .claim(EMAIL, account.getEmail())
                              .claim(NAME, account.getName())
                              .claim(AVATAR_URL, account.getAvatarUrl())
                              .claim(ROLE, account.getRoleKey())
                              .signWith(SignatureAlgorithm.HS256, jwtProperties.getTokenSecret())
                              .compact();
        return jwtProperties.getHeaderPrefix() + jwtToken;
    }

    public boolean validateJwtToken(String authorization) {
        String jwtToken = extractToken(authorization);
        try {
            Jws<Claims> claims = Jwts.parser()
                                     .setSigningKey(jwtProperties.getTokenSecret())
                                     .parseClaimsJws(jwtToken);
            return claims.getBody()
                         .getExpiration()
                         .after(new Date());
        } catch (JwtException exception) {
            System.out.println("유효기간 만료");
            throw new JwtValidationException();
        }
    }

    public AccountClaims parseJwtToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                                     .setSigningKey(jwtProperties.getTokenSecret())
                                     .parseClaimsJws(jwtToken);
            Claims body = claims.getBody();

            return AccountClaims.from(body);
        } catch (JwtException exception) {
            throw new JwtValidationException();
        }
    }

    public String extractToken(String authorizationHeader) {
        if (isTokenContained(authorizationHeader)) {
            int tokenBeginIndex = jwtProperties.getHeaderPrefix().length();
            return StringUtils.substring(authorizationHeader, tokenBeginIndex);
        }

        throw new JwtValidationException();
    }

    private boolean isTokenContained(String authorization) {
        if (StringUtils.isEmpty(authorization)) {
            return false;
        }

        return authorization.startsWith(jwtProperties.getHeaderPrefix());
    }
}
