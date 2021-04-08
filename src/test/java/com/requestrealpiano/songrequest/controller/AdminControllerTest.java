package com.requestrealpiano.songrequest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.requestrealpiano.songrequest.domain.letter.request.ChangeReadyRequest;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import com.requestrealpiano.songrequest.security.SecurityConfig;
import com.requestrealpiano.songrequest.security.oauth.OAuthAccount;
import com.requestrealpiano.songrequest.service.AdminService;
import com.requestrealpiano.songrequest.testconfig.BaseControllerTest;
import com.requestrealpiano.songrequest.testconfig.security.mockuser.WithAdmin;
import com.requestrealpiano.songrequest.testconfig.security.mockuser.WithMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.requestrealpiano.songrequest.controller.MockMvcRequest.post;
import static com.requestrealpiano.songrequest.domain.account.Role.ADMIN;
import static com.requestrealpiano.songrequest.domain.account.Role.MEMBER;
import static com.requestrealpiano.songrequest.testobject.AccountFactory.createOAuthAccountOf;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = AdminController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
class AdminControllerTest extends BaseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AdminService adminService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithAdmin
    @DisplayName("OK - Letter 등록 상태 변경 API 테스트")
    void change_ready_to_letter() throws Exception {
        // given
        OAuthAccount adminAccount = createOAuthAccountOf(ADMIN);
        ChangeReadyRequest changeReadyRequest = new ChangeReadyRequest();
        changeReadyRequest.setReadyToLetter(TRUE);

        // when
        when(adminService.changeReadyToLetter(refEq(changeReadyRequest))).thenReturn(TRUE);

        ResultActions results = mockMvc.perform(post("/api/admin/letters/ready")
                                                .withPrincipal(adminAccount)
                                                .withBody(objectMapper.writeValueAsString(changeReadyRequest))
                                                .doRequest());

        // then
        MockMvcResponse.OK(results);
    }

    @Test
    @WithMember
    @DisplayName("FORBIDDEN - ADMIN 권한이 없는 사용자가 Letter 등록 상태 변경시 예외가 발생하는 테스트")
    void forbidden_change_ready_to_letter() throws Exception {
        // given
        ErrorCode accessDeniedError = ErrorCode.ACCESS_DENIED_ERROR;
        OAuthAccount memberAccount = createOAuthAccountOf(MEMBER);
        ChangeReadyRequest changeReadyRequest = new ChangeReadyRequest();
        changeReadyRequest.setReadyToLetter(FALSE);

        // when
        when(adminService.changeReadyToLetter(refEq(changeReadyRequest))).thenReturn(FALSE);

        ResultActions results = mockMvc.perform(post("/api/admin/letters/ready")
                                                .withPrincipal(memberAccount)
                                                .withBody(objectMapper.writeValueAsString(changeReadyRequest))
                                                .doRequest());

        // then
        MockMvcResponse.FORBIDDEN(results, accessDeniedError);
    }
}
