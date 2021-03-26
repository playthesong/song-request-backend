package com.requestrealpiano.songrequest.domain.letter;

import com.requestrealpiano.songrequest.testconfig.BaseRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.requestrealpiano.songrequest.domain.letter.RequestStatus.*;
import static com.requestrealpiano.songrequest.testobject.LetterFactory.createLetter;
import static com.requestrealpiano.songrequest.testobject.LetterFactory.createLetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LetterRepositoryTest extends BaseRepositoryTest {

    @Autowired
    LetterRepository letterRepository;

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
}
