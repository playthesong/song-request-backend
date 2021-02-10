package com.requestrealpiano.songrequest.global.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    @DisplayName("표준 응답 객체 생성 테스트 - OK")
    void create_new_api_response_OK() {
        // given
        String testMessage = "Test Message";
        String OK_MESSAGE = "OK";

        // when
        ApiResponse<String> ApiResponseOK = ApiResponse.OK(testMessage);

        // then
        assertAll(
                () -> assertThat(ApiResponseOK.isSuccess()).isTrue(),
                () -> assertThat(ApiResponseOK.getStatusMessage()).isEqualTo(OK_MESSAGE),
                () -> assertThat(ApiResponseOK.getData()).isEqualTo(testMessage)
        );
    }
}
