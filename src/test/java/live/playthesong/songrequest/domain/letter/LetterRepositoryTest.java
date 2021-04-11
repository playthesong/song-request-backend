package live.playthesong.songrequest.domain.letter;

import live.playthesong.songrequest.domain.letter.request.PaginationParameters;
import live.playthesong.songrequest.testconfig.BaseRepositoryTest;
import live.playthesong.songrequest.global.constant.SortProperties;
import live.playthesong.songrequest.testobject.LetterFactory;
import live.playthesong.songrequest.testobject.PaginationFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LetterRepositoryTest extends BaseRepositoryTest {

    @Autowired
    LetterRepository letterRepository;

    @ParameterizedTest
    @MethodSource("paginationFindAllLettersParameters")
    @DisplayName("Paging 처리된 Letter 목록 조회 테스트")
    void pagination_find_all_letters(List<Letter> letters, Integer page, Integer size, String direction) {
        // given
        PaginationParameters parameters = PaginationFactory.createPaginationParametersOf(page, size, direction);
        PageRequest pageRequest = PageRequest.of(parameters.getPage(), parameters.getSize(),  Sort.by(Direction.DESC, SortProperties.CREATED_DATE_TIME.getFieldName()));

        // when
        letterRepository.saveAll(letters);
        Page<Letter> savedLetters = letterRepository.findAll(pageRequest);

        // then
        assertThat(savedLetters.getContent().size()).isEqualTo(size);
        assertThat(savedLetters.getTotalElements()).isEqualTo(letters.size());
    }

    @Test
    @DisplayName("RequestStatus를 기준으로 Letter 목록을 조회하는 테스트")
    void find_all_by_request_status() {
        // given
        PageRequest letterPage = PaginationFactory.createPageRequest();
        List<Letter> letters = LetterFactory.createLetters();
        LocalDateTime endDateTime = scheduler.now();
        LocalDateTime startDateTime = scheduler.defaultStartDateTimeFrom(endDateTime);

        // when
        letterRepository.saveAll(letters);
        Page<Letter> waitingLetters = letterRepository.findAllTodayLettersByRequestStatus(letterPage, RequestStatus.WAITING, startDateTime, endDateTime);
        Page<Letter> pendingLetters = letterRepository.findAllTodayLettersByRequestStatus(letterPage, RequestStatus.PENDING, startDateTime, endDateTime);
        Page<Letter> doneLetters = letterRepository.findAllTodayLettersByRequestStatus(letterPage, RequestStatus.DONE, startDateTime, endDateTime);

        // then
        assertAll(
                () -> assertThat(waitingLetters.stream()
                                               .allMatch(letter -> letter.getRequestStatus().equals(RequestStatus.WAITING))).isTrue(),
                () -> assertThat(pendingLetters.stream()
                                               .allMatch(letter -> letter.getRequestStatus().equals(RequestStatus.PENDING))).isTrue(),
                () -> assertThat(doneLetters.stream()
                                            .allMatch(letter -> letter.getRequestStatus().equals(RequestStatus.DONE))).isTrue()
        );
    }

    @Test
    @DisplayName("JPA Auditing 적용 Letter 생성 시간 테스트")
    void created_date_time_of_letter() {
        // given
        Letter letter = LetterFactory.createLetter();
        LocalDateTime testDateTime = LocalDateTime.now();

        // when
        Letter savedLetter = letterRepository.save(letter);

        // then
        Assertions.assertThat(savedLetter.getCreatedDateTime()).isNotNull();
        Assertions.assertThat(savedLetter.getCreatedDateTime()).isAfter(testDateTime);
    }

    private static Stream<Arguments> paginationFindAllLettersParameters() {
        Integer page = 1;
        Integer size = 20;
        String direction = "ASC";
        List<Letter> letters = new ArrayList<>();

        for (int i = 0; i < 10 ; i++) {
            letters.addAll(LetterFactory.createLetters());
        }

        return Stream.of(
                Arguments.of(letters, page, size, direction)
        );
    }
}
