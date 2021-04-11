package live.playthesong.songrequest.domain.letter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class RequestStatusTest {

    @ParameterizedTest
    @MethodSource("validRequestStatusParameters")
    @DisplayName("올바른 상태 변경을 요청 했을 때 JSON 변환이 완료 되는 테스트")
    void valid_request_status(String validStatus) {
        // when
        RequestStatus status = RequestStatus.createJson(validStatus);

        // then
        assertThatCode(() -> RequestStatus.createJson(validStatus)).doesNotThrowAnyException();
        assertThat(status.getKey()).isEqualToIgnoringCase(validStatus);
    }

    @ParameterizedTest
    @MethodSource("invalidRequestStatusParameters")
    void invalid_request_status(String invalidStatus) {

        // then
        assertThat(RequestStatus.createJson(invalidStatus)).isNull();
    }

    private static Stream<Arguments> invalidRequestStatusParameters() {
        return Stream.of(
            Arguments.of("notStatus"), Arguments.of("1234567"), Arguments.of("Mixed - 1")
        );
    }

    private static Stream<Arguments> validRequestStatusParameters() {
        return Stream.of(
                Arguments.of("waiting"),
                Arguments.of("PENDING"),
                Arguments.of("Done")
        );
    }
}
