package com.requestrealpiano.songrequest.global.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    @DisplayName("표준 응답 객체 생성 테스트")
    void create_new_api_response_OK() {
        // given
        String testMessage = "Test Message";
        String OK_MESSAGE = "OK";
        String CREATED_MESSAGE = "CREATED";
        String UPDATED_MESSAGE = "UPDATED";
        String DELETED_MESSAGE = "DELETED";


        // when
        ApiResponse<String> ApiResponseOK = ApiResponse.OK(testMessage);
        ApiResponse<Void> ApiResponseCREATED = ApiResponse.CREATED();
        ApiResponse<Void> ApiResponseUPDATED = ApiResponse.UPDATED();
        ApiResponse<Void> ApiResponseDELETED = ApiResponse.DELETED();

        // then
        assertAll(
                () -> assertThat(ApiResponseOK.isSuccess()).isTrue(),
                () -> assertThat(ApiResponseOK.getStatusMessage()).isEqualTo(OK_MESSAGE),
                () -> assertThat(ApiResponseOK.getData()).isEqualTo(testMessage)
        );

        assertAll(
                () -> assertThat(ApiResponseCREATED.isSuccess()).isTrue(),
                () -> assertThat(ApiResponseCREATED.getStatusMessage()).isEqualTo(CREATED_MESSAGE),
                () -> assertThat(ApiResponseCREATED.getData()).isNull()
        );

        assertAll(
                () -> assertThat(ApiResponseUPDATED.isSuccess()).isTrue(),
                () -> assertThat(ApiResponseUPDATED.getStatusMessage()).isEqualTo(UPDATED_MESSAGE),
                () -> assertThat(ApiResponseUPDATED.getData()).isNull()
        );

        assertAll(
                () -> assertThat(ApiResponseDELETED.isSuccess()).isTrue(),
                () -> assertThat(ApiResponseDELETED.getStatusMessage()).isEqualTo(DELETED_MESSAGE),
                () -> assertThat(ApiResponseDELETED.getData()).isNull()
        );
    }

}
