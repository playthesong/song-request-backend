package live.playthesong.songrequest.controller.song;

import live.playthesong.songrequest.controller.MockMvcResponse;
import live.playthesong.songrequest.domain.account.Account;
import live.playthesong.songrequest.domain.account.AccountRepository;
import live.playthesong.songrequest.domain.song.searchapi.maniadb.ManiaDbRestClient;
import live.playthesong.songrequest.domain.song.searchapi.request.SearchSongParameters;
import live.playthesong.songrequest.global.error.response.ErrorCode;
import live.playthesong.songrequest.testconfig.BaseIntegrationTest;
import live.playthesong.songrequest.controller.MockMvcRequest;
import live.playthesong.songrequest.testobject.JwtFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.file.Files;
import java.nio.file.Path;

import static live.playthesong.songrequest.controller.MockMvcRequest.get;
import static live.playthesong.songrequest.testobject.AccountFactory.createMember;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
        String jwtToken = JwtFactory.createValidJwtTokenOf(account);

        // when
        when(maniaDbRestClient.searchManiaDb(any(SearchSongParameters.class))).thenReturn(testXmlResponse);

        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/songs")
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
        String invalidJwtToken = JwtFactory.createInvalidJwtTokenOf(account);
        ErrorCode invalidJwtError = ErrorCode.JWT_INVALID_ERROR;

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/songs")
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
        String expiredJwtToken = JwtFactory.createExpiredJwtTokenOf(account);
        ErrorCode jwtExpirationError = ErrorCode.JWT_EXPIRATION_ERROR;

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/songs")
                                                .withParam("artist", artist)
                                                .withParam("title", title)
                                                .withToken(expiredJwtToken)
                                                .doRequest());

        // then
        MockMvcResponse.UNAUTHORIZED(results, jwtExpirationError);
    }
}
