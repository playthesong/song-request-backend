package com.requestrealpiano.songrequest.controller.letter;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.domain.letter.request.NewLetterRequest;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import com.requestrealpiano.songrequest.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultActions;

import static com.requestrealpiano.songrequest.testobject.AccountFactory.createMember;
import static com.requestrealpiano.songrequest.testobject.JwtFactory.createInvalidJwtTokenOf;
import static com.requestrealpiano.songrequest.testobject.JwtFactory.createValidJwtTokenOf;
import static com.requestrealpiano.songrequest.testobject.LetterFactory.createNewLetterRequestOf;
import static com.requestrealpiano.songrequest.testobject.SongFactory.createSongRequest;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LetterIntegrationTest extends BaseIntegrationTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    @DisplayName("Valid JWT - 유효한 JWT 토큰과 함께 Letter 생성을 요청하는 테스트")
    void with_valid_jwt_create_letter() throws Exception {
        // given
        Account account = accountRepository.save(createMember());
        SongRequest songRequest = createSongRequest();
        NewLetterRequest newLetterRequest = createNewLetterRequestOf("Song Story", songRequest, account.getId());
        String jwtToken = createValidJwtTokenOf(account);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/letters")
                                                      .accept(APPLICATION_JSON)
                                                      .contentType(APPLICATION_JSON_VALUE)
                                                      .header(HttpHeaders.AUTHORIZATION, jwtToken)
                                                      .content(objectMapper.writeValueAsString(newLetterRequest)));

        // then
        resultActions.andDo(print())
                     .andExpect(status().isOk())
                     .andExpect(jsonPath("success").value(true))
                     .andExpect(jsonPath("statusMessage").value("OK"))
                     .andExpect(jsonPath("data").isNotEmpty())
        ;
    }

    @Test
    @DisplayName("Invalid JWT - 유효하지 않은 JWT 토큰과 함께 Letter 생성을 요청하는 테스트")
    void with_invalid_jwt_create_letter() throws Exception {
        // given
        Account account = accountRepository.save(createMember());
        SongRequest songRequest = createSongRequest();
        NewLetterRequest newLetterRequest = createNewLetterRequestOf("Song Story", songRequest, account.getId());
        String invalidJwtToken = createInvalidJwtTokenOf(account);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/letters")
                                                      .accept(APPLICATION_JSON)
                                                      .contentType(APPLICATION_JSON_VALUE)
                                                      .header(HttpHeaders.AUTHORIZATION, invalidJwtToken)
                                                      .content(objectMapper.writeValueAsString(newLetterRequest)));

        // then
        resultActions.andDo(print())
                     .andExpect(status().isBadRequest())
                     .andExpect(jsonPath("statusCode").value(ErrorCode.JWT_INVALID_ERROR.getStatusCode()))
                     .andExpect(jsonPath("message").value(ErrorCode.JWT_INVALID_ERROR.getMessage()))
                     .andExpect(jsonPath("errors").isEmpty())
        ;
    }
}
