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

    @Test
    @DisplayName("표준 응답 객체 생성 테스트 - CREATED")
    void create_new_api_response_CREATED() {
        // given
        String CREATED_MESSAGE = "CREATED";

        // when
        ApiResponse<Void> ApiResponseCREATED = ApiResponse.CREATED();

        // then
        assertAll(
                () -> assertThat(ApiResponseCREATED.isSuccess()).isTrue(),
                () -> assertThat(ApiResponseCREATED.getStatusMessage()).isEqualTo(CREATED_MESSAGE),
                () -> assertThat(ApiResponseCREATED.getData()).isNull()
        );
    }

    @Test
    @DisplayName("표준 응답 객체 생성 테스트 - UPDATED")
    void create_new_api_response_UPDATED() {
        // given
        String UPDATED_MESSAGE = "UPDATED";

        // when
        ApiResponse<Void> ApiResponseUPDATED = ApiResponse.UPDATED();

        // then
        assertAll(
                () -> assertThat(ApiResponseUPDATED.isSuccess()).isTrue(),
                () -> assertThat(ApiResponseUPDATED.getStatusMessage()).isEqualTo(UPDATED_MESSAGE),
                () -> assertThat(ApiResponseUPDATED.getData()).isNull()
        );
    }

    @Test
    @DisplayName("표준 응답 객체 생성 테스트 - DELETED")
    void create_new_api_response_DELETED() {
        // given
        String DELETED_MESSAGE = "DELETED";

        // when
        ApiResponse<Void> ApiResponseDELETED = ApiResponse.DELETED();

        // then
        assertAll(
                () -> assertThat(ApiResponseDELETED.isSuccess()).isTrue(),
                () -> assertThat(ApiResponseDELETED.getStatusMessage()).isEqualTo(DELETED_MESSAGE),
                () -> assertThat(ApiResponseDELETED.getData()).isNull()
        );
    }
}
