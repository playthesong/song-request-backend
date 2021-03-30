package com.requestrealpiano.songrequest.service.letter;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.LetterRepository;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.domain.song.SongRepository;
import com.requestrealpiano.songrequest.global.error.exception.BusinessException;
import com.requestrealpiano.songrequest.global.error.exception.business.AccountMismatchException;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import com.requestrealpiano.songrequest.global.time.Scheduler;
import com.requestrealpiano.songrequest.security.oauth.OAuthAccount;
import com.requestrealpiano.songrequest.service.LetterService;
import com.requestrealpiano.songrequest.service.SongService;
import com.requestrealpiano.songrequest.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static com.requestrealpiano.songrequest.testobject.AccountFactory.createMember;
import static com.requestrealpiano.songrequest.testobject.SongFactory.createSong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LetterServiceJpaTest extends BaseIntegrationTest {

    @Autowired
    LetterService letterService;

    @Autowired
    SongService songService;

    @Autowired
    LetterRepository letterRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    Scheduler scheduler;

    @Test
    @DisplayName("SUCCESS - Letter 삭제 테스트")
    void success_delete() {
        // given
        String songStory = "Song Story";

        Account createdAccount = accountRepository.save(createMember());
        Song createdSong = songRepository.save(createSong());
        List<Letter> letters = Arrays.asList(Letter.of(songStory, createdAccount, createdSong),
                                             Letter.of(songStory, createdAccount, createdSong),
                                             Letter.of(songStory, createdAccount, createdSong));

        List<Letter> createdLetters = letterRepository.saveAll(letters);
        OAuthAccount loginAccount = OAuthAccount.from(createdAccount);

        int first = 0;
        Long deleteLetterId = createdLetters.get(first).getId();

        // when
        long beforeCount = createdLetters.size();
        letterService.deleteLetter(loginAccount, deleteLetterId);
        long afterCount = letterRepository.count();

        // then
        assertThat(afterCount).isEqualTo(beforeCount - 1);
    }

    @Test
    @DisplayName("AccountMismatchException - Letter 삭제 테스트")
    void accountmismatch_delete() {
        // given
        ErrorCode accountMismatchError = ErrorCode.ACCOUNT_MISMATCH_ERROR;

        String songStory = "Song Story";

        Account loginAccount = accountRepository.save(createMember());
        Account notLoginAccount = accountRepository.save(createMember());
        Song createdSong = songRepository.save(createSong());
        Letter letter = Letter.of(songStory, loginAccount, createdSong);
        OAuthAccount anotherAccount = OAuthAccount.from(notLoginAccount);

        Letter createdLetter = letterRepository.save(letter);

        // then
        assertThatThrownBy(() -> letterService.deleteLetter(anotherAccount, createdLetter.getId()))
                .isExactlyInstanceOf(AccountMismatchException.class)
                .isInstanceOf(BusinessException.class)
                .hasMessage(accountMismatchError.getMessage());
    }
}