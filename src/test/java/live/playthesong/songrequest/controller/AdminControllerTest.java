package live.playthesong.songrequest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.playthesong.songrequest.domain.letter.request.ChangeReadyRequest;
import live.playthesong.songrequest.global.admin.Admin;
import live.playthesong.songrequest.global.error.response.ErrorCode;
import live.playthesong.songrequest.security.SecurityConfig;
import live.playthesong.songrequest.security.oauth.OAuthAccount;
import live.playthesong.songrequest.service.AdminService;
import live.playthesong.songrequest.testconfig.BaseControllerTest;
import live.playthesong.songrequest.testconfig.security.mockuser.WithAdmin;
import live.playthesong.songrequest.testconfig.security.mockuser.WithMember;
import live.playthesong.songrequest.domain.account.Role;
import live.playthesong.songrequest.testobject.AccountFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static live.playthesong.songrequest.controller.MockMvcRequest.get;
import static live.playthesong.songrequest.testobject.AccountFactory.createOAuthAccountOf;
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

    @SpyBean
    Admin admin;

    @Test
    @WithAdmin
    @DisplayName("OK - Letter 등록 가능 여부 조회 API 테스트")
    void ok_ready_to_letter() throws Exception {
        // given
        OAuthAccount adminAccount = AccountFactory.createOAuthAccountOf(Role.ADMIN);

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/admin/letters/ready")
                                                .withPrincipal(adminAccount)
                                                .doRequest());

        // then
        MockMvcResponse.OK(results);
    }

    @Test
    @WithMember
    @DisplayName("FORBIDDEN - ADMIN 권한이 없는 사용자가 Letter 등록 상태 조회를 요청할 때 예외가 발생하는 테스트")
    void forbidden_ready_to_letter() throws Exception {
        // given
        ErrorCode accessDeniedError = ErrorCode.ACCESS_DENIED_ERROR;
        OAuthAccount memberAccount = AccountFactory.createOAuthAccountOf(Role.MEMBER);

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/admin/letters/ready")
                                                .withPrincipal(memberAccount)
                                                .doRequest());

        // then
        MockMvcResponse.FORBIDDEN(results, accessDeniedError);
    }

    @Test
    @WithAdmin
    @DisplayName("OK - Letter 등록 상태 변경 API 테스트")
    void change_ready_to_letter() throws Exception {
        // given
        OAuthAccount adminAccount = AccountFactory.createOAuthAccountOf(Role.ADMIN);
        ChangeReadyRequest changeReadyRequest = new ChangeReadyRequest();
        changeReadyRequest.setReadyToLetter(TRUE);

        // when
        when(adminService.changeReadyToLetter(refEq(changeReadyRequest))).thenReturn(TRUE);

        ResultActions results = mockMvc.perform(MockMvcRequest.post("/api/admin/letters/ready")
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
        OAuthAccount memberAccount = AccountFactory.createOAuthAccountOf(Role.MEMBER);
        ChangeReadyRequest changeReadyRequest = new ChangeReadyRequest();
        changeReadyRequest.setReadyToLetter(FALSE);

        // when
        when(adminService.changeReadyToLetter(refEq(changeReadyRequest))).thenReturn(FALSE);

        ResultActions results = mockMvc.perform(MockMvcRequest.post("/api/admin/letters/ready")
                                                .withPrincipal(memberAccount)
                                                .withBody(objectMapper.writeValueAsString(changeReadyRequest))
                                                .doRequest());

        // then
        MockMvcResponse.FORBIDDEN(results, accessDeniedError);
    }


}
