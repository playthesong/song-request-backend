package live.playthesong.songrequest.service.letter;

import live.playthesong.songrequest.domain.account.Account;
import live.playthesong.songrequest.domain.account.AccountRepository;
import live.playthesong.songrequest.domain.letter.Letter;
import live.playthesong.songrequest.domain.letter.LetterRepository;
import live.playthesong.songrequest.domain.song.Song;
import live.playthesong.songrequest.domain.song.SongRepository;
import live.playthesong.songrequest.global.error.exception.BusinessException;
import live.playthesong.songrequest.global.error.exception.business.AccountMismatchException;
import live.playthesong.songrequest.global.error.response.ErrorCode;
import live.playthesong.songrequest.global.time.Scheduler;
import live.playthesong.songrequest.security.oauth.OAuthAccount;
import live.playthesong.songrequest.service.LetterService;
import live.playthesong.songrequest.service.SongService;
import live.playthesong.songrequest.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static live.playthesong.songrequest.testobject.AccountFactory.createAdmin;
import static live.playthesong.songrequest.testobject.AccountFactory.createMember;
import static live.playthesong.songrequest.testobject.SongFactory.createSong;
import static org.assertj.core.api.Assertions.*;

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
    @DisplayName("SUCCESS - ID는 다르지만 권한이 ADMIN인 경우 성공하는 테스트")
    void admin_delete() {
        // given
        String songStory = "Song Story";

        Account letterAccount = accountRepository.save(createMember());
        Account adminAccount = accountRepository.save(createAdmin());
        Song createdSong = songRepository.save(createSong());
        Letter letter = Letter.of(songStory, letterAccount, createdSong);
        Letter createdLetter = letterRepository.save(letter);

        OAuthAccount admin = OAuthAccount.from(adminAccount);

        // when
        long beforeCount = letterRepository.count();
        letterService.deleteLetter(admin, createdLetter.getId());
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
