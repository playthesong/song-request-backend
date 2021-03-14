package com.requestrealpiano.songrequest.controller.account;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import com.requestrealpiano.songrequest.service.AccountService;
import com.requestrealpiano.songrequest.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.requestrealpiano.songrequest.testobject.AccountFactory.createMember;
import static com.requestrealpiano.songrequest.testobject.JwtFactory.createExpiredGenerationKeyOf;
import static com.requestrealpiano.songrequest.testobject.JwtFactory.createInvalidGenerationKeyOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @DisplayName("Invalid JWT - Invalid GenerationKey 를 사용한 JWT 생성 요청 테스트")
    void with_invalid_jwt_generate_token() throws Exception {
        // given
        String invalidGenerationKey = createInvalidGenerationKeyOf(account.getEmail());
        ErrorCode jwtInvalidError = ErrorCode.JWT_INVALID_ERROR;

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/accounts/auth")
                                                      .accept(MediaType.APPLICATION_JSON)
                                                      .header(HttpHeaders.AUTHORIZATION, invalidGenerationKey));

        // then
        resultActions.andDo(print())
                     .andExpect(status().isBadRequest())
                     .andExpect(jsonPath("statusCode").value(jwtInvalidError.getStatusCode()))
                     .andExpect(jsonPath("message").value(jwtInvalidError.getMessage()))
        ;
    }

    @Test
    @DisplayName("Expired JWT - 만료된 GenerationKey 를 사용한 JWT 생성 요청 테스트")
    void with_expired_jwt_generate_token() throws Exception {
        // given
        String expiredGenerationKey = createExpiredGenerationKeyOf(account.getEmail());
        ErrorCode jwtExpirationError = ErrorCode.JWT_EXPIRATION_ERROR;

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/accounts/auth")
                                                      .accept(MediaType.APPLICATION_JSON)
                                                      .header(HttpHeaders.AUTHORIZATION, expiredGenerationKey));

        // then
        resultActions.andDo(print())
                     .andExpect(status().isUnauthorized())
                     .andExpect(jsonPath("statusCode").value(jwtExpirationError.getStatusCode()))
                     .andExpect(jsonPath("message").value(jwtExpirationError.getMessage()))
        ;
    }
}
