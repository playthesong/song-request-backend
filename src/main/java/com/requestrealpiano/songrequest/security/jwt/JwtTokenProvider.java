package com.requestrealpiano.songrequest.security.jwt;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.global.error.exception.jwt.JwtExpirationException;
import com.requestrealpiano.songrequest.global.error.exception.jwt.JwtInvalidTokenException;
import com.requestrealpiano.songrequest.global.error.exception.jwt.JwtRequiredException;
import com.requestrealpiano.songrequest.security.jwt.claims.AccountClaims;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
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
        long period = Long.parseLong(jwtProperties.getGenerationKeyExpiration());
        ZonedDateTime now = ZonedDateTime.now();

        return Jwts.builder()
                   .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                   .setIssuedAt(Date.from(now.toInstant()))
                   .setExpiration(Date.from(now.toInstant().plusMillis(period)))
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
        } catch (ExpiredJwtException exception) {
            throw new JwtExpirationException();
        } catch (JwtException exception) {
            throw new JwtInvalidTokenException();
        }
    }

    public String createJwtToken(Account account) {
        long period = Long.parseLong(jwtProperties.getTokenExpiration());
        ZonedDateTime now = ZonedDateTime.now();

        String jwtToken = Jwts.builder()
                              .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                              .setIssuedAt(Date.from(now.toInstant()))
                              .setExpiration(Date.from(now.toInstant().plusMillis(period)))
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
        } catch (ExpiredJwtException exception) {
            throw new JwtExpirationException();
        } catch (JwtException exception) {
            throw new JwtInvalidTokenException();
        }
    }

    public AccountClaims parseJwtToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                                     .setSigningKey(jwtProperties.getTokenSecret())
                                     .parseClaimsJws(jwtToken);
            Claims body = claims.getBody();

            return AccountClaims.from(body);
        } catch (ExpiredJwtException exception) {
            throw new JwtExpirationException();
        } catch (JwtException exception) {
            throw new JwtInvalidTokenException();
        }
    }

    public String extractToken(String authorizationHeader) {
        if (isTokenContained(authorizationHeader)) {
            int tokenBeginIndex = jwtProperties.getHeaderPrefix().length();
            return StringUtils.substring(authorizationHeader, tokenBeginIndex);
        }
        throw new JwtRequiredException();
    }

    private boolean isTokenContained(String authorization) {
        if (StringUtils.isEmpty(authorization)) {
            return false;
        }

        return authorization.startsWith(jwtProperties.getHeaderPrefix());
    }
}
