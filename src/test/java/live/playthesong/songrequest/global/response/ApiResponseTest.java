package live.playthesong.songrequest.global.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    @DisplayName("표준 응답 객체 생성 테스트 - OK")
    void create_new_api_response_OK() {
        // given
        StatusCode statusCode = StatusCode.OK;
        String testData = "test data";

        // when
        ApiResponse<String> ApiResponseOK = ApiResponse.SUCCESS(statusCode, testData);

        // then
        assertAll(
                () -> assertThat(ApiResponseOK.getStatusCode()).isEqualTo(statusCode.getCode()),
                () -> assertThat(ApiResponseOK.getData()).isEqualTo(testData)
        );
    }
}
