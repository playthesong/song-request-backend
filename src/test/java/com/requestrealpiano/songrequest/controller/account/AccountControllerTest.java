package com.requestrealpiano.songrequest.controller.account;

import com.requestrealpiano.songrequest.controller.AccountController;
import com.requestrealpiano.songrequest.controller.MockMvcResponse;
import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountDetail;
import com.requestrealpiano.songrequest.security.SecurityConfig;
import com.requestrealpiano.songrequest.security.oauth.OAuthAccount;
import com.requestrealpiano.songrequest.service.AccountService;
import com.requestrealpiano.songrequest.testconfig.BaseControllerTest;
import com.requestrealpiano.songrequest.testconfig.security.mockuser.WithMember;
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

import static com.requestrealpiano.songrequest.controller.MockMvcRequest.get;
import static com.requestrealpiano.songrequest.domain.account.Role.MEMBER;
import static com.requestrealpiano.songrequest.testobject.AccountFactory.*;
import static com.requestrealpiano.songrequest.testobject.JwtFactory.createValidGenerationKeyOf;
import static com.requestrealpiano.songrequest.testobject.JwtFactory.createValidJwtTokenOf;
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
        String validGenerationKey = createValidGenerationKeyOf(account.getEmail());
        String jwtToken = createValidJwtTokenOf(account);

        // when
        when(accountService.generateJwtToken(validGenerationKey)).thenReturn(jwtToken);

        ResultActions results = mockMvc.perform(get("/api/accounts/auth")
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

        ResultActions results = mockMvc.perform(get("/api/accounts/detail")
                                                .withPrincipal(loginAccount)
                                                .doRequest());

        // then
        MockMvcResponse.OK(results);
    }
}
