package com.requestrealpiano.songrequest.controller.account;

import com.requestrealpiano.songrequest.controller.MockMvcResponse;
import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import com.requestrealpiano.songrequest.service.AccountService;
import com.requestrealpiano.songrequest.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static com.requestrealpiano.songrequest.controller.MockMvcRequest.get;
import static com.requestrealpiano.songrequest.testobject.AccountFactory.createMember;
import static com.requestrealpiano.songrequest.testobject.JwtFactory.*;

public class AccountIntegrationTest extends BaseIntegrationTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    private Account account;

    @BeforeEach
    void setup() {
        account = accountRepository.save(createMember());
    }

    @Test
    @DisplayName("Invalid Generation Key - Invalid GenerationKey 를 사용한 JWT 생성 요청 테스트")
    void with_invalid_jwt_generate_token() throws Exception {
        // given
        String invalidGenerationKey = createInvalidGenerationKeyOf(account.getEmail());
        ErrorCode jwtInvalidError = ErrorCode.JWT_INVALID_ERROR;

        // when
        ResultActions results = mockMvc.perform(get("/api/accounts/auth")
                                                .withToken(invalidGenerationKey)
                                                .doRequest());

        // then
        MockMvcResponse.BAD_REQUEST(results, jwtInvalidError);
    }

    @Test
    @DisplayName("Expired Generation Key - 만료된 GenerationKey 를 사용한 JWT 생성 요청 테스트")
    void with_expired_jwt_generate_token() throws Exception {
        // given
        String expiredGenerationKey = createExpiredGenerationKeyOf(account.getEmail());
        ErrorCode jwtExpirationError = ErrorCode.JWT_EXPIRATION_ERROR;

        // when
        ResultActions results = mockMvc.perform(get("/api/accounts/auth")
                                                .withToken(expiredGenerationKey)
                                                .doRequest());

        // then
        MockMvcResponse.UNAUTHORIZED(results, jwtExpirationError);
    }

    @Test
    @DisplayName("Valid JWT Token - 유효한 JWT Token을 사용한 Validation 요청 테스트")
    void validate_with_valid_jwt() throws Exception {
        // given
        String validJwtToken = createValidJwtTokenOf(account);

        // when
        ResultActions results = mockMvc.perform(get("/api/accounts/auth/validation")
                                                .withToken(validJwtToken)
                                                .doRequest());

        // then
        MockMvcResponse.NO_CONTENT(results);
    }

    @Test
    @DisplayName("Invalid JWT Token - 만료된 JWT Token을 사용한 Validation 요청 테스트")
    void validate_with_invalid_jwt() throws Exception {
        // given
        String invalidJwtToken = createInvalidJwtTokenOf(account);
        ErrorCode jwtInvalidError = ErrorCode.JWT_INVALID_ERROR;

        // when
        ResultActions results = mockMvc.perform(get("/api/accounts/validation")
                                                .withToken(invalidJwtToken)
                                                .doRequest());

        // then
        MockMvcResponse.BAD_REQUEST(results, jwtInvalidError);
    }

    @Test
    @DisplayName("Expired JWT Token - 만료된 JWT Token을 사용한 Validation 요청 테스트")
    void validate_with_expired_jwt() throws Exception {
        // given
        String expiredJwtToken = createExpiredJwtTokenOf(account);
        ErrorCode jwtExpirationError = ErrorCode.JWT_EXPIRATION_ERROR;

        // when
        ResultActions results = mockMvc.perform(get("/api/accounts/validation")
                                                .withToken(expiredJwtToken)
                                                .doRequest());

        // then
        MockMvcResponse.UNAUTHORIZED(results, jwtExpirationError);
    }
}
