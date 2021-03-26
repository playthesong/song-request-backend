package com.requestrealpiano.songrequest.domain.letter;

import com.requestrealpiano.songrequest.domain.letter.request.PaginationParameters;
import com.requestrealpiano.songrequest.global.constant.SortProperties;
import com.requestrealpiano.songrequest.testconfig.BaseRepositoryTest;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.requestrealpiano.songrequest.domain.letter.RequestStatus.*;
import static com.requestrealpiano.songrequest.global.constant.SortProperties.CREATED_DATE_TIME;
import static com.requestrealpiano.songrequest.testobject.AccountFactory.createMember;
import static com.requestrealpiano.songrequest.testobject.LetterFactory.*;
import static com.requestrealpiano.songrequest.testobject.PaginationFactory.createPaginationParametersOf;
import static com.requestrealpiano.songrequest.testobject.SongFactory.createSong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LetterRepositoryTest extends BaseRepositoryTest {

    @Autowired
    LetterRepository letterRepository;

    @ParameterizedTest
    @MethodSource("paginationFindAllLettersParameters")
    @DisplayName("Paging 처리된 Letter 목록 조회 테스트")
    void pagination_find_all_letters(List<Letter> letters, Integer page, Integer size) {
        // given
        PaginationParameters parameters = createPaginationParametersOf(page, size);
        PageRequest pageRequest = PageRequest.of(parameters.getPage(), parameters.getSize(),  Sort.by(Direction.DESC, CREATED_DATE_TIME.getFieldName()));

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
        List<Letter> letters = createLetters();

        // when
        letterRepository.saveAll(letters);
        List<Letter> waitingLetters = letterRepository.findAllByRequestStatus(WAITING);
        List<Letter> pendingLetters = letterRepository.findAllByRequestStatus(PENDING);
        List<Letter> doneLetters = letterRepository.findAllByRequestStatus(DONE);

        // then
        assertAll(
                () -> assertThat(waitingLetters.stream()
                                               .allMatch(letter -> letter.getRequestStatus().equals(WAITING))).isTrue(),
                () -> assertThat(pendingLetters.stream()
                                               .allMatch(letter -> letter.getRequestStatus().equals(PENDING))).isTrue(),
                () -> assertThat(doneLetters.stream()
                                            .allMatch(letter -> letter.getRequestStatus().equals(DONE))).isTrue()
        );
    }

    @Test
    @DisplayName("JPA Auditing 적용 Letter 생성 시간 테스트")
    void created_date_time_of_letter() {
        // given
        Letter letter = createLetter();
        LocalDateTime testDateTime = LocalDateTime.now();

        // when
        Letter savedLetter = letterRepository.save(letter);

        // then
        assertThat(savedLetter.getCreatedDateTime()).isNotNull();
        assertThat(savedLetter.getCreatedDateTime()).isAfter(testDateTime);
    }

    private static Stream<Arguments> paginationFindAllLettersParameters() {
        Integer page = 1;
        Integer size = 20;
        List<Letter> letters = new ArrayList<>();

        for (int i = 0; i < 10 ; i++) {
            letters.addAll(createLetters());
        }

        return Stream.of(
                Arguments.of(letters, page, size)
        );
    }
}
