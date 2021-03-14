package com.requestrealpiano.songrequest.controller.letter;

import com.requestrealpiano.songrequest.controller.MockMvcRequest;
import com.requestrealpiano.songrequest.controller.MockMvcResponse;
import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.domain.letter.request.NewLetterRequest;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import com.requestrealpiano.songrequest.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static com.requestrealpiano.songrequest.testobject.AccountFactory.createMember;
import static com.requestrealpiano.songrequest.testobject.JwtFactory.*;
import static com.requestrealpiano.songrequest.testobject.LetterFactory.createNewLetterRequestOf;
import static com.requestrealpiano.songrequest.testobject.SongFactory.createSongRequest;

public class LetterIntegrationTest extends BaseIntegrationTest {

    @Autowired
    AccountRepository accountRepository;

    private Account account;
    private SongRequest songRequest;
    private NewLetterRequest newLetterRequest;

    @BeforeEach
    void setup() {
        account = accountRepository.save(createMember());
        songRequest = createSongRequest();
        newLetterRequest = createNewLetterRequestOf("Song Story", songRequest, account.getId());
    }

    @Test
    @DisplayName("Valid JWT - 유효한 JWT 토큰과 함께 Letter 생성을 요청하는 테스트")
    void with_valid_jwt_create_letter() throws Exception {
        // given
        String jwtToken = createValidJwtTokenOf(account);
        String requestBody = objectMapper.writeValueAsString(newLetterRequest);

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.post("/api/letters", requestBody, jwtToken));

        // then
        MockMvcResponse.OK(results);
    }

    @Test
    @DisplayName("Invalid JWT - 유효하지 않은 JWT 토큰과 함께 Letter 생성을 요청하는 테스트")
    void with_invalid_jwt_create_letter() throws Exception {
        // given
        String invalidJwtToken = createInvalidJwtTokenOf(account);
        String requestBody = objectMapper.writeValueAsString(newLetterRequest);
        ErrorCode jwtInvalidError = ErrorCode.JWT_INVALID_ERROR;

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.post("/api/letters", requestBody, invalidJwtToken));

        // then
        MockMvcResponse.BAD_REQUEST(results, jwtInvalidError);
    }

    @Test
    @DisplayName("Expired JWT - 만료된 JWT 토큰과 함께 Letter 생성을 요청하는 테스트")
    void with_expired_jwt_create_letter() throws Exception {
        // given
        String expiredJwtToken = createExpiredJwtTokenOf(account);
        String requestBody = objectMapper.writeValueAsString(newLetterRequest);
        ErrorCode jwtExpirationError = ErrorCode.JWT_EXPIRATION_ERROR;

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.post("/api/letters", requestBody, expiredJwtToken));

        // then
        MockMvcResponse.UNAUTHORIZED(results, jwtExpirationError);
    }
}
