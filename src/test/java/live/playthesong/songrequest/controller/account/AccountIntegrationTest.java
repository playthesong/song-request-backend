package live.playthesong.songrequest.controller.account;

import live.playthesong.songrequest.controller.MockMvcResponse;
import live.playthesong.songrequest.domain.account.Account;
import live.playthesong.songrequest.domain.account.AccountRepository;
import live.playthesong.songrequest.global.error.response.ErrorCode;
import live.playthesong.songrequest.service.AccountService;
import live.playthesong.songrequest.testconfig.BaseIntegrationTest;
import live.playthesong.songrequest.controller.MockMvcRequest;
import live.playthesong.songrequest.testobject.JwtFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static live.playthesong.songrequest.controller.MockMvcRequest.get;
import static live.playthesong.songrequest.testobject.AccountFactory.createMember;

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
        String invalidGenerationKey = JwtFactory.createInvalidGenerationKeyOf(account.getEmail());
        ErrorCode jwtInvalidError = ErrorCode.JWT_INVALID_ERROR;

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/accounts/auth")
                                                .withToken(invalidGenerationKey)
                                                .doRequest());

        // then
        MockMvcResponse.BAD_REQUEST(results, jwtInvalidError);
    }

    @Test
    @DisplayName("Expired Generation Key - 만료된 GenerationKey 를 사용한 JWT 생성 요청 테스트")
    void with_expired_jwt_generate_token() throws Exception {
        // given
        String expiredGenerationKey = JwtFactory.createExpiredGenerationKeyOf(account.getEmail());
        ErrorCode jwtExpirationError = ErrorCode.JWT_EXPIRATION_ERROR;

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/accounts/auth")
                                                .withToken(expiredGenerationKey)
                                                .doRequest());

        // then
        MockMvcResponse.UNAUTHORIZED(results, jwtExpirationError);
    }

    @Test
    @DisplayName("Valid JWT Token - 유효한 JWT Token을 사용한 Validation 요청 테스트")
    void validate_with_valid_jwt() throws Exception {
        // given
        String validJwtToken = JwtFactory.createValidJwtTokenOf(account);

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/accounts/auth/validation")
                                                .withToken(validJwtToken)
                                                .doRequest());

        // then
        MockMvcResponse.NO_CONTENT(results);
    }

    @Test
    @DisplayName("Invalid JWT Token - 만료된 JWT Token을 사용한 Validation 요청 테스트")
    void validate_with_invalid_jwt() throws Exception {
        // given
        String invalidJwtToken = JwtFactory.createInvalidJwtTokenOf(account);
        ErrorCode jwtInvalidError = ErrorCode.JWT_INVALID_ERROR;

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/accounts/validation")
                                                .withToken(invalidJwtToken)
                                                .doRequest());

        // then
        MockMvcResponse.BAD_REQUEST(results, jwtInvalidError);
    }

    @Test
    @DisplayName("Expired JWT Token - 만료된 JWT Token을 사용한 Validation 요청 테스트")
    void validate_with_expired_jwt() throws Exception {
        // given
        String expiredJwtToken = JwtFactory.createExpiredJwtTokenOf(account);
        ErrorCode jwtExpirationError = ErrorCode.JWT_EXPIRATION_ERROR;

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/accounts/validation")
                                                .withToken(expiredJwtToken)
                                                .doRequest());

        // then
        MockMvcResponse.UNAUTHORIZED(results, jwtExpirationError);
    }
}
