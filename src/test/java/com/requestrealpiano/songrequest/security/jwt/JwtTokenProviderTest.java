package com.requestrealpiano.songrequest.security.jwt;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.global.error.exception.JwtValidationException;
import com.requestrealpiano.songrequest.global.error.exception.jwt.JwtRequiredException;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import com.requestrealpiano.songrequest.security.jwt.claims.AccountClaims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.requestrealpiano.songrequest.testobject.AccountFactory.createMember;
import static com.requestrealpiano.songrequest.testobject.JwtFactory.createValidJwtProperties;
import static com.requestrealpiano.songrequest.testobject.JwtFactory.createValidJwtTokenOf;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @InjectMocks
    JwtTokenProvider jwtTokenProvider;

    @Mock
    JwtProperties jwtProperties;

    @Test
    @DisplayName("유효한 Authorization Header로부터 JwtToken을 추출하는 테스트")
    void with_valid_authorization_extract_token() {
        // given
        JwtProperties properties = createValidJwtProperties();
        String jwtToken = createValidJwtTokenOf(createMember());
        String validAuthorizationHeader = properties.getHeaderPrefix() + jwtToken;

        // when
        when(jwtProperties.getHeaderPrefix()).thenReturn(properties.getHeaderPrefix());
        String extractedJwtToken = jwtTokenProvider.extractToken(validAuthorizationHeader);

        // then
        assertThat(extractedJwtToken).isEqualTo(jwtToken);
        assertThatCode(() -> jwtTokenProvider.extractToken(validAuthorizationHeader)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("유효하지 않은 Authorization Header로 요청했을 경우 예외 발생 테스트")
    void with_invalid_authorization_extract_token() {
        // given
        JwtProperties properties = createValidJwtProperties();
        String invalidAuthorizationHeader = "Invalid Header";
        ErrorCode jwtRequiredError = ErrorCode.UNAUTHENTICATED_ERROR;

        // when
        when(jwtProperties.getHeaderPrefix()).thenReturn(properties.getHeaderPrefix());


        // then
        assertThatThrownBy(() -> jwtTokenProvider.extractToken(invalidAuthorizationHeader))
                .isExactlyInstanceOf(JwtRequiredException.class)
                .isInstanceOf(JwtValidationException.class)
                .hasMessage(jwtRequiredError.getMessage());
    }

    @Test
    @DisplayName("Generation Key 테스트")
    void create_then_parse_generation_key() {
        // given
        JwtProperties properties = createValidJwtProperties();
        Account account = createMember();
        String email = account.getEmail();

        // when
        when(jwtProperties.getHeaderPrefix()).thenReturn(properties.getHeaderPrefix());
        when(jwtProperties.getGenerationKeyExpiration()).thenReturn(properties.getGenerationKeyExpiration());
        when(jwtProperties.getGenerationKeySecret()).thenReturn(properties.getGenerationKeySecret());
        String generationKey = jwtTokenProvider.createGenerationKey(email);
        String generationKeyEmail = jwtTokenProvider.parseGenerationKey(properties.getHeaderPrefix() + generationKey);

        // then
        assertThat(generationKey).isNotEmpty();
        assertThat(generationKeyEmail).isEqualTo(email);
    }

    @Test
    @DisplayName("JWT Token 테스트")
    void create_then_parse_jwt_token() {
        // given
        JwtProperties properties = createValidJwtProperties();
        Account account = createMember();

        // when
        when(jwtProperties.getHeaderPrefix()).thenReturn(properties.getHeaderPrefix());
        when(jwtProperties.getTokenExpiration()).thenReturn(properties.getTokenExpiration());
        when(jwtProperties.getTokenSecret()).thenReturn(properties.getTokenSecret());

        String bearerToken = jwtTokenProvider.createJwtToken(account);
        String jwtToken = jwtTokenProvider.extractToken(bearerToken);
        AccountClaims accountClaims = jwtTokenProvider.parseJwtToken(jwtToken);

        // then
        assertAll(
                () -> assertThat(jwtToken).isNotEmpty(),
                () -> assertThat(account.getEmail()).isEqualTo(accountClaims.getEmail()),
                () -> assertThat(account.getAvatarUrl()).isEqualTo(accountClaims.getAvatarUrl())
        );
    }
}
