package com.requestrealpiano.songrequest.domain.letter;

import com.requestrealpiano.songrequest.testconfig.BaseRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LetterRepositoryTest extends BaseRepositoryTest {

    @Autowired
    LetterRepository letterRepository;

    @Test
    @DisplayName("Letter 데이터를 DB에서 조회한다.")
    void find_all_letters() {
        // given
        List<Letter> letters = createMockLetters();

        // when
        letterRepository.saveAll(letters);
        List<Letter> savedLetters = letterRepository.findAll();

        /* 0, 1, 2 - List Index */
        Letter firstLetter = letters.get(0);
        Letter secondLetter = letters.get(1);
        Letter thirdLetter = letters.get(2);

        // then
        assertThat(savedLetters).hasSize(letters.size());
        assertThat(savedLetters.stream().filter(letter -> Optional.ofNullable(letter.getId()).isPresent())
                               .count()).isEqualTo(letters.size());

        /* 1L, 2L, 3L - Auto Increment 생성 ID */
        assertThat(firstLetter.getId()).isEqualTo(1L);
        assertThat(secondLetter.getId()).isEqualTo(2L);
        assertThat(thirdLetter.getId()).isEqualTo(3L);
    }

    private List<Letter> createMockLetters() {
        Letter firstLetter = Letter.builder()
                                   .songStory("Song Story 1")
                                   .requestStatus(RequestStatus.WAITING)
                                   .build();
        Letter secondLetter = Letter.builder()
                                    .songStory("Song Story 2")
                                    .requestStatus(RequestStatus.WAITING)
                                    .build();
        Letter thirdLetter = Letter.builder()
                                   .songStory("Song Story 3")
                                   .requestStatus(RequestStatus.WAITING)
                                   .build();
        return Arrays.asList(firstLetter, secondLetter, thirdLetter);
    }
}
