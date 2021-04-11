package live.playthesong.songrequest.domain.letter;

import live.playthesong.songrequest.domain.letter.request.PaginationParameters;
import live.playthesong.songrequest.testconfig.BaseRepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static live.playthesong.songrequest.domain.letter.RequestStatus.WAITING;
import static live.playthesong.songrequest.global.constant.SortProperties.CREATED_DATE_TIME;
import static live.playthesong.songrequest.testobject.LetterFactory.createLetters;
import static live.playthesong.songrequest.testobject.PaginationFactory.createPaginationParametersOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

public class LetterRepositoryDateTimeTest extends BaseRepositoryTest {

    @Autowired
    LetterRepository letterRepository;

    @SpyBean
    protected AuditingHandler auditingHandler;

    @MockBean
    protected DateTimeProvider dateTimeProvider;

    @BeforeEach
    public void setup() {
        auditingHandler.setDateTimeProvider(dateTimeProvider);
    }

    @ParameterizedTest
    @MethodSource("findLettersByDateTimeParameters")
    @DisplayName("ALL - 설정된 시간에 해당 되는 Letter 목록을 가져오는 테스트")
    void datetime_find_all_letters(List<Letter> foundLetters, List<Letter> notFoundLetters,
                                   Integer page, Integer pageSize, String direction) {
        /* 하루 전 19시 부터 현재시간 까지의 Letters 를 모두 조회하는 테스트 */

        // given
        PaginationParameters parameters = createPaginationParametersOf(page, pageSize, direction);
        PageRequest pageRequest = PageRequest.of(parameters.getPage(), parameters.getSize(),  Sort.by(Sort.Direction.DESC, CREATED_DATE_TIME.getFieldName()));

        LocalDateTime foundTime = LocalDateTime.of(2021, 3, 26, 19, 0, 0);
        LocalDateTime notFoundTime = LocalDateTime.of(2021, 3, 26, 18, 59, 59);
        LocalDateTime now = LocalDateTime.of(2021, 3, 27, 23, 45, 0);
        LocalDateTime startDateTime = scheduler.defaultStartDateTimeFrom(now);

        // when
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(notFoundTime));
        letterRepository.saveAll(notFoundLetters);

        when(dateTimeProvider.getNow()).thenReturn(Optional.of(foundTime));
        letterRepository.saveAll(foundLetters);

        when(dateTimeProvider.getNow()).thenReturn(Optional.of(now));
        Page<Letter> todayLetters = letterRepository.findAllTodayLetters(pageRequest, startDateTime, now);

        long totalCountOfLetters = letterRepository.count();

        // then
        assertAll(
                () -> assertThat(todayLetters.getContent().size()).isEqualTo(foundLetters.size()),
                () -> assertThat(todayLetters.getContent().stream()
                        .allMatch(letter -> letter.getCreatedDateTime().isAfter(notFoundTime)))
                        .isTrue(),
                () -> assertThat(todayLetters.getContent().stream()
                        .allMatch(letter -> letter.getCreatedDateTime().isBefore(now)))
                        .isTrue(),
                () -> assertThat(totalCountOfLetters).isEqualTo(notFoundLetters.size() + todayLetters.getNumberOfElements())
        );
    }

    @ParameterizedTest
    @MethodSource("findLettersByDateTimeParameters")
    @DisplayName("By RequestStatus - 설정된 시간에 해당 되는 Letter 목록을 가져오는 테스트")
    void datetime_find_letters_by_status(List<Letter> foundLetters, List<Letter> notFoundLetters,
                                         Integer page, Integer pageSize, String direction) {
        /* 하루 전 19시 부터 현재시간 까지의 Letters 를 Status 기준으로 조회하는 테스트 */

        // given
        PaginationParameters parameters = createPaginationParametersOf(page, pageSize, direction);
        PageRequest pageRequest = PageRequest.of(parameters.getPage(), parameters.getSize(),  Sort.by(Sort.Direction.DESC, CREATED_DATE_TIME.getFieldName()));

        LocalDateTime foundTime = LocalDateTime.of(2021, 3, 27, 19, 0, 0);
        LocalDateTime notFoundTime = LocalDateTime.of(2021, 3, 27, 18, 59, 59);
        LocalDateTime now = LocalDateTime.of(2021, 3, 28, 23, 45, 0);
        LocalDateTime startDateTime = scheduler.defaultStartDateTimeFrom(now);

        // when
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(notFoundTime));
        letterRepository.saveAll(notFoundLetters);

        when(dateTimeProvider.getNow()).thenReturn(Optional.of(foundTime));
        letterRepository.saveAll(foundLetters);

        when(dateTimeProvider.getNow()).thenReturn(Optional.of(now));
        Page<Letter> waitingLetters = letterRepository.findAllTodayLettersByRequestStatus(pageRequest, WAITING, startDateTime, now);

        // then
        assertAll(
                () -> assertThat(waitingLetters.getContent().size())
                        .isEqualTo(foundLetters.stream()
                                               .filter(letter -> letter.getRequestStatus().equals(WAITING))
                                               .count()),
                () -> assertThat(waitingLetters.getContent().stream()
                        .allMatch(letter -> letter.getCreatedDateTime().isAfter(notFoundTime)))
                        .isTrue(),
                () -> assertThat(waitingLetters.getContent().stream()
                        .allMatch(letter -> letter.getCreatedDateTime().isBefore(now)))
                        .isTrue()
        );
    }

    @ParameterizedTest
    @MethodSource("initializeLettersParameters")
    @DisplayName("Letters initialization 테스트")
    void initialize_letters(List<Letter> deletedLetters, long deletedLettersSize,
                            List<Letter> notDeletedLetters, long notDeletedLettersSize) {
        /* 하루 전 18시 59분 부터 현재시간 까지의 Letters 를 삭제하는 테스트 */

        // given
        LocalDateTime deletedTime = LocalDateTime.of(2021, 4, 7, 18, 59, 0);
        LocalDateTime notDeletedTime = LocalDateTime.of(2021, 4, 7, 18, 58, 59);
        LocalDateTime now = LocalDateTime.of(2021, 4, 8, 15, 5, 0);
        LocalDateTime startDateTime = scheduler.initializationStartDateTimeFrom(now);

        // when
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(notDeletedTime));
        letterRepository.saveAll(notDeletedLetters);

        when(dateTimeProvider.getNow()).thenReturn(Optional.of(deletedTime));
        letterRepository.saveAll(deletedLetters);

        long todayLettersCount = letterRepository.count();

        when(dateTimeProvider.getNow()).thenReturn(Optional.of(now));
        letterRepository.deleteAllTodayLetters(startDateTime, now);

        long notDeletedCount = letterRepository.count();
        long deletedCount = todayLettersCount - notDeletedCount;

        // then
        assertAll(
                () -> assertThat(notDeletedCount).isEqualTo(notDeletedLettersSize),
                () -> assertThat(deletedCount).isEqualTo(deletedLettersSize)
        );
    }

    private static Stream<Arguments> initializeLettersParameters() {
        List<Letter> deletedLetters = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            deletedLetters.addAll(createLetters());
        }

        List<Letter> notDeletedLetters = createLetters();

        return Stream.of(
                Arguments.of(
                    deletedLetters, (long) deletedLetters.size(), notDeletedLetters, (long) notDeletedLetters.size()
                )
        );
    }

    private static Stream<Arguments> findLettersByDateTimeParameters() {
        Integer page = 1;
        Integer size = 20;
        String direction = "ASC";

        List<Letter> foundLetters = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            foundLetters.addAll(createLetters());
        }

        List<Letter> notFoundLetters = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            notFoundLetters.addAll(createLetters());
        }

        return Stream.of(
                Arguments.of(foundLetters, notFoundLetters, page, size, direction)
        );
    }
}
