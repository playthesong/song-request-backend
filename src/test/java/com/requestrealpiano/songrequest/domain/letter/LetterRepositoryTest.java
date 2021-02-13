package com.requestrealpiano.songrequest.domain.letter;

import com.requestrealpiano.songrequest.testconfig.BaseRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
                          String firstSongStory, String secondSongStory, String thirdSongStory) {
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

        assertThat(firstLetter.getSongStory()).isEqualTo(firstSongStory);
        assertThat(secondLetter.getSongStory()).isEqualTo(secondSongStory);
        assertThat(thirdLetter.getSongStory()).isEqualTo(thirdSongStory);
    }

    private static Stream<Arguments> findAllLettersParameters() {
        return Stream.of(
                Arguments.of(0, 1, 2, "Song Story 1", "Song Story 2", "Song Story 3")
        );
    }

    @ParameterizedTest
    @MethodSource("createdDateTimeOfLetterParameters")
    @DisplayName("JPA Auditing 적용 Letter 생성 시간 테스트")
    void created_date_time_of_letter(LocalDateTime testDateTime, int first) {
        // given
        List<Letter> letters = createMockLetters();
        Letter letter = letters.get(first);

        // when
        Letter savedLetter = letterRepository.save(letter);

        // then
        assertThat(savedLetter.getCreatedDateTime()).isNotNull();
        assertThat(savedLetter.getCreatedDateTime()).isAfter(testDateTime);
    }

    private static Stream<Arguments> createdDateTimeOfLetterParameters() {
        return Stream.of(
                Arguments.of(LocalDateTime.now(), 0)
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
