package com.requestrealpiano.songrequest.controller.letter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.requestrealpiano.songrequest.controller.MockMvcResponse;
import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.domain.letter.request.LetterRequest;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import com.requestrealpiano.songrequest.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static com.requestrealpiano.songrequest.controller.MockMvcRequest.post;
import static com.requestrealpiano.songrequest.testobject.AccountFactory.createMember;
import static com.requestrealpiano.songrequest.testobject.JwtFactory.*;
import static com.requestrealpiano.songrequest.testobject.LetterFactory.createLetterRequestOf;
import static com.requestrealpiano.songrequest.testobject.SongFactory.createSongRequest;

public class LetterIntegrationTest extends BaseIntegrationTest {

    @Autowired
    AccountRepository accountRepository;

    private Account account;
    private SongRequest songRequest;
    private LetterRequest letterRequest;
    private String requestBody;

    @BeforeEach
    void setup() throws JsonProcessingException {
        account = accountRepository.save(createMember());
        songRequest = createSongRequest();
        letterRequest = createLetterRequestOf("Song Story", songRequest);
        requestBody = objectMapper.writeValueAsString(letterRequest);
    }

    @Test
    @DisplayName("Valid JWT - 유효한 JWT 토큰과 함께 Letter 생성을 요청하는 테스트")
    void with_valid_jwt_create_letter() throws Exception {
        // given
        String jwtToken = createValidJwtTokenOf(account);

        // when
        ResultActions results = mockMvc.perform(post("/api/letters")
                                                .withBody(requestBody)
                                                .withToken(jwtToken)
                                                .doRequest());

        // then
        MockMvcResponse.CREATED(results);
    }

    @Test
    @DisplayName("Invalid JWT - 유효하지 않은 JWT 토큰과 함께 Letter 생성을 요청하는 테스트")
    void with_invalid_jwt_create_letter() throws Exception {
        // given
        String invalidJwtToken = createInvalidJwtTokenOf(account);
        ErrorCode jwtInvalidError = ErrorCode.JWT_INVALID_ERROR;

        // when
        ResultActions results = mockMvc.perform(post("/api/letters")
                                                .withBody(requestBody)
                                                .withToken(invalidJwtToken)
                                                .doRequest());

        // then
        MockMvcResponse.BAD_REQUEST(results, jwtInvalidError);
    }

    @Test
    @DisplayName("Expired JWT - 만료된 JWT 토큰과 함께 Letter 생성을 요청하는 테스트")
    void with_expired_jwt_create_letter() throws Exception {
        // given
        String expiredJwtToken = createExpiredJwtTokenOf(account);
        ErrorCode jwtExpirationError = ErrorCode.JWT_EXPIRATION_ERROR;

        // when
        ResultActions results = mockMvc.perform(post("/api/letters")
                                                .withBody(requestBody)
                                                .withToken(expiredJwtToken)
                                                .doRequest());

        // then
        MockMvcResponse.UNAUTHORIZED(results, jwtExpirationError);
    }
}
