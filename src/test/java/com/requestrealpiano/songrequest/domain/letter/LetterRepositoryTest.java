package com.requestrealpiano.songrequest.domain.letter;

import com.requestrealpiano.songrequest.testconfig.BaseRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LetterRepositoryTest extends BaseRepositoryTest {

    @Autowired
    LetterRepository letterRepository;

    @ParameterizedTest
    @MethodSource("findAllLettersParameters")
    @DisplayName("Letter 데이터를 DB에서 조회한다.")
    void find_all_letters(int first, int second, int third,
                          Long firstLetterId, Long secondLetterId, Long thirdLetterId) {
        // given
        List<Letter> letters = createMockLetters();

        // when
        letterRepository.saveAll(letters);
        List<Letter> savedLetters = letterRepository.findAll();

        Letter firstLetter = letters.get(first);
        Letter secondLetter = letters.get(second);
        Letter thirdLetter = letters.get(third);

        // then
        assertThat(savedLetters).hasSize(letters.size());
        assertThat(savedLetters.stream().filter(letter -> Optional.ofNullable(letter.getId()).isPresent())
                               .count()).isEqualTo(letters.size());

        assertThat(firstLetter.getId()).isEqualTo(firstLetterId);
        assertThat(secondLetter.getId()).isEqualTo(secondLetterId);
        assertThat(thirdLetter.getId()).isEqualTo(thirdLetterId);
    }

    private static Stream<Arguments> findAllLettersParameters() {
        return Stream.of(
                Arguments.of(0, 1, 2, 1L, 2L, 3L)
        );
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
