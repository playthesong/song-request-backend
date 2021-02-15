package com.requestrealpiano.songrequest.domain.letter;

import com.requestrealpiano.songrequest.testconfig.BaseRepositoryTest;
import com.requestrealpiano.songrequest.testobject.LetterFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LetterRepositoryTest extends BaseRepositoryTest {

    @Autowired
    LetterRepository letterRepository;

    @Test
    @DisplayName("Letter 데이터를 DB에서 조회한다.")
    void find_all_letters() {
        // given
        List<Letter> letters = LetterFactory.createList();

        // when
        letterRepository.saveAll(letters);
        List<Letter> savedLetters = letterRepository.findAll();

        // then
        assertThat(savedLetters).hasSize(letters.size());
        assertThat(savedLetters.stream().filter(letter -> Optional.ofNullable(letter.getId()).isPresent())
                               .count()).isEqualTo(letters.size());
    }

    @Test
    @DisplayName("JPA Auditing 적용 Letter 생성 시간 테스트")
    void created_date_time_of_letter() {
        // given
        Letter letter = LetterFactory.createOne();
        LocalDateTime testDateTime = LocalDateTime.now();

        // when
        Letter savedLetter = letterRepository.save(letter);

        // then
        assertThat(savedLetter.getCreatedDateTime()).isNotNull();
        assertThat(savedLetter.getCreatedDateTime()).isAfter(testDateTime);
    }
}
