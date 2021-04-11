package live.playthesong.songrequest.domain.letter.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.DESC;

class PaginationParametersTest {

    PaginationParameters parameters;

    @BeforeEach
    void setup() {
        parameters = new PaginationParameters();
    }

    @Test
    @DisplayName("Page number 테스트")
    void set_page() {
        // given

        // 클라이언트에서 요청한 페이지 번호를 JPA 에서 사용하기 위해 -1 을 해줍니다.
        int requestSize = 6;
        int actualSize = 5;

        // when
        parameters.setPage(requestSize);

        // then
        assertThat(parameters.getPage()).isEqualTo(actualSize);
    }

    @ParameterizedTest
    @MethodSource("boundaryValueSetPageParameters")
    @DisplayName("Page number 경계 값 테스트")
    void boundary_value_set_page(Integer page, Integer pageMinimum) {
        // when
        parameters.setPage(page);

        // then
        assertThat(parameters.getPage()).isEqualTo(pageMinimum);
    }

    @Test
    @DisplayName("Page size 테스트")
    void set_size() {
        // given
        int size = 28;

        // when
        parameters.setSize(size);

        // then
        assertThat(parameters.getSize()).isEqualTo(size);
    }

    @ParameterizedTest
    @MethodSource("boundaryValueSetSizeParameters")
    @DisplayName("Page size 경계 값 테스트")
    void boundary_value_set_size(Integer size, Integer defaultSize) {
        // when
        parameters.setSize(size);

        // then
        assertThat(parameters.getSize()).isEqualTo(defaultSize);
    }

    @ParameterizedTest
    @MethodSource("setDirectionParameters")
    @DisplayName("Direction 테스트")
    void set_direction(String direction) {
        // when
        parameters.setDirection(direction);

        // then
        assertThat(parameters.getDirection()).isEqualTo(direction);
    }

    @ParameterizedTest
    @MethodSource("boundaryValueSetDirectionParameters")
    @DisplayName("Sort Direction 경계 값 테스트")
    void boundary_value_set_direction(String direction, String defaultDirection) {
        // when
        parameters.setDirection(direction);

        // then
        assertThat(parameters.getDirection()).isEqualTo(defaultDirection);
    }

    private static Stream<Arguments> boundaryValueSetPageParameters() {
        Integer defaultPage = 0;
        return Stream.of(
                Arguments.of(0, defaultPage), Arguments.of(null, defaultPage)
        );
    }

    private static Stream<Arguments> boundaryValueSetSizeParameters() {
        Integer defaultSize = 20;
        return Stream.of(
                Arguments.of(null, defaultSize), Arguments.of(9, defaultSize), Arguments.of(51, defaultSize)
        );
    }

    private static Stream<Arguments> setDirectionParameters() {
        return Stream.of(
                Arguments.of("asc"), Arguments.of("ASC"), Arguments.of("desc"), Arguments.of("DESC")
        );
    }

    private static Stream<Arguments> boundaryValueSetDirectionParameters() {
        String defaultDirection = DESC.name();
        return Stream.of(
                Arguments.of(null, defaultDirection), Arguments.of("", defaultDirection), Arguments.of("Something", defaultDirection)
        );
    }
}
