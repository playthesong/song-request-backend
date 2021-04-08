package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.letter.request.ChangeReadyRequest;
import com.requestrealpiano.songrequest.global.admin.Admin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    AdminService adminService;

    @Spy
    Admin admin;

    @Test
    @DisplayName("Letter 등록 가능 상태를 변경한 뒤 반환하는 테스트")
    void change_ready_to_letter() {
        // given
        ChangeReadyRequest request = new ChangeReadyRequest();
        request.setReadyToLetter(TRUE);

        // when
        Boolean isReadyToLetter = adminService.changeReadyToLetter(request);

        // then
        assertThat(isReadyToLetter).isTrue();
    }
}
