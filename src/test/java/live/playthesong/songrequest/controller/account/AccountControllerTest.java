package live.playthesong.songrequest.controller.account;

import live.playthesong.songrequest.controller.AccountController;
import live.playthesong.songrequest.controller.MockMvcResponse;
import live.playthesong.songrequest.domain.account.Account;
import live.playthesong.songrequest.domain.account.AccountDetail;
import live.playthesong.songrequest.security.SecurityConfig;
import live.playthesong.songrequest.security.oauth.OAuthAccount;
import live.playthesong.songrequest.service.AccountService;
import live.playthesong.songrequest.testconfig.BaseControllerTest;
import live.playthesong.songrequest.testconfig.security.mockuser.WithMember;
import live.playthesong.songrequest.controller.MockMvcRequest;
import live.playthesong.songrequest.testobject.JwtFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static live.playthesong.songrequest.controller.MockMvcRequest.delete;
import static live.playthesong.songrequest.controller.MockMvcRequest.get;
import static live.playthesong.songrequest.domain.account.Role.MEMBER;
import static live.playthesong.songrequest.testobject.AccountFactory.*;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@WebMvcTest(controllers = AccountController.class,
            excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
class AccountControllerTest extends BaseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    @Test
    @DisplayName("Valid JWT - GenerationKey 인증 이후 JWT 생성 테스트")
    void with_valid_jwt_generate_token() throws Exception {
        // given
        Account account = createMember();
        String validGenerationKey = JwtFactory.createValidGenerationKeyOf(account.getEmail());
        String jwtToken = JwtFactory.createValidJwtTokenOf(account);

        // when
        when(accountService.generateJwtToken(validGenerationKey)).thenReturn(jwtToken);

        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/accounts/auth")
                                                .withToken(validGenerationKey)
                                                .doRequest());

        // then
        MockMvcResponse.NO_CONTENT(results)
                       .andExpect(header().string(HttpHeaders.AUTHORIZATION, jwtToken));
    }

    @Test
    @WithMember
    @DisplayName("OK - Account detail API 테스트")
    void account_detail() throws Exception {
        // given
        OAuthAccount loginAccount = createOAuthAccountOf(MEMBER);
        Account account = createMemberOf(loginAccount.getId());

        // when
        when(accountService.findAccountDetail(refEq(loginAccount))).thenReturn(AccountDetail.from(account));

        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/accounts/detail")
                                                .withPrincipal(loginAccount)
                                                .doRequest());

        // then
        MockMvcResponse.OK(results);
    }

    @Test
    @WithMember
    @DisplayName("NO_CONTENT - Account 삭제 API 테스트")
    void delete_account() throws Exception {
        // given
        OAuthAccount loginAccount = createOAuthAccountOf(MEMBER);

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.delete("/api/accounts")
                                                .withPrincipal(loginAccount)
                                                .doRequest());
        
        // then
        MockMvcResponse.NO_CONTENT(results);
    }
}
