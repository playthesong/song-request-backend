package com.requestrealpiano.songrequest.controller.song;

import com.requestrealpiano.songrequest.controller.MockMvcRequest;
import com.requestrealpiano.songrequest.controller.MockMvcResponse;
import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.ManiaDbRestClient;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import com.requestrealpiano.songrequest.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.requestrealpiano.songrequest.controller.MockMvcRequest.get;
import static com.requestrealpiano.songrequest.testobject.AccountFactory.createMember;
import static com.requestrealpiano.songrequest.testobject.JwtFactory.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SongIntegrationTest extends BaseIntegrationTest {

    @Autowired
    AccountRepository accountRepository;

    @MockBean
    ManiaDbRestClient maniaDbRestClient;

    private Account account;
    private String title;
    private String artist;

    @BeforeEach
    void setup() {
        account = accountRepository.save(createMember());
        title = "Title";
        artist = "Artist";
    }

    @Test
    @DisplayName("Valid JWT - 유효한 JWT 토큰과 함께 Song 검색 테스트")
    void with_valid_jwt_search_song() throws Exception {
        // given
        Path xmlPath = Path.of("src/test/resources/expectedresponse/maniadb/maniadb_response.xml");
        String testXmlResponse = Files.readString(xmlPath);
        String jwtToken = createValidJwtTokenOf(account);

        // when
        when(maniaDbRestClient.searchManiaDb(artist, title)).thenReturn(testXmlResponse);

        ResultActions results = mockMvc.perform(get("/api/songs")
                                                .withParam("artist", artist)
                                                .withParam("title", title)
                                                .withToken(jwtToken)
                                                .doRequest());

        // then
        MockMvcResponse.OK(results);
    }

    @Test
    @DisplayName("Invalid JWT - 유효하지 않은 JWT 토큰과 함께 Song 검색 테스트")
    void with_invalid_jwt_search_song() throws Exception {
        // given
        String invalidJwtToken = createInvalidJwtTokenOf(account);
        ErrorCode invalidJwtError = ErrorCode.JWT_INVALID_ERROR;

        // when
        ResultActions results = mockMvc.perform(get("/api/songs")
                                                .withParam("artist", artist)
                                                .withParam("title", title)
                                                .withToken(invalidJwtToken)
                                                .doRequest());

        // then
        MockMvcResponse.BAD_REQUEST(results, invalidJwtError);
    }

    @Test
    @DisplayName("Expired JWT - 만료된 JWT 토큰과 함께 Song 검색 테스트")
    void with_expired_jwt_search_song() throws Exception {
        // given
        String expiredJwtToken = createExpiredJwtTokenOf(account);
        ErrorCode jwtExpirationError = ErrorCode.JWT_EXPIRATION_ERROR;

        // when
        ResultActions results = mockMvc.perform(get("/api/songs")
                                                .withParam("artist", artist)
                                                .withParam("title", title)
                                                .withToken(expiredJwtToken)
                                                .doRequest());

        // then
        MockMvcResponse.UNAUTHORIZED(results, jwtExpirationError);
    }
}
