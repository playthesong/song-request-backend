package com.requestrealpiano.songrequest.service.account;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.LetterRepository;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.domain.song.SongRepository;
import com.requestrealpiano.songrequest.security.oauth.OAuthAccount;
import com.requestrealpiano.songrequest.service.AccountService;
import com.requestrealpiano.songrequest.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static com.requestrealpiano.songrequest.testobject.AccountFactory.createMember;
import static com.requestrealpiano.songrequest.testobject.SongFactory.createSong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AccountServiceJpaTest extends BaseIntegrationTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    LetterRepository letterRepository;

    @Autowired
    SongRepository songRepository;

    @Test
    @DisplayName("Account를 삭제하는 테스트")
    void delete_account() {
        // given
        int initialCount = 1;
        int afterDeleteCount = 0;

        String songStory = "songStory";

        Account account = accountRepository.save(createMember());

        Song song = songRepository.save(createSong());
        List<Letter> letters = Arrays.asList(Letter.of(songStory, account, song),
                                             Letter.of(songStory, account, song),
                                             Letter.of(songStory, account, song));

        List<Letter> savedLetters = letterRepository.saveAll(letters);
        OAuthAccount loginAccount = OAuthAccount.from(account);

        // when
        long beforeLetterCount = letterRepository.count();
        long beforeAccountCount = accountRepository.count();
        accountService.deleteAccount(loginAccount);

        long afterLetterCount = letterRepository.count();
        long afterAccountCount = accountRepository.count();
        // then
        assertAll(
                () -> assertThat(beforeLetterCount).isEqualTo(savedLetters.size()),
                () -> assertThat(afterLetterCount).isEqualTo(afterDeleteCount),
                () -> assertThat(beforeAccountCount).isEqualTo(initialCount),
                () -> assertThat(afterAccountCount).isEqualTo(afterDeleteCount)
        );
    }
}
