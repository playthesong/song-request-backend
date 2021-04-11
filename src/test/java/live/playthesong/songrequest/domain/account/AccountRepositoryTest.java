package live.playthesong.songrequest.domain.account;

import live.playthesong.songrequest.security.oauth.OAuthAttributes;
import live.playthesong.songrequest.testconfig.BaseRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static live.playthesong.songrequest.testobject.AccountFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

class AccountRepositoryTest extends BaseRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    @DisplayName("Account 생성 시 RequestCount 기본 값 설정 테스트")
    void create_account_default_request_count() {
        // given
        OAuthAttributes oAuthAttributes = createOAuthAttributes();
        Account account = createMemberOf(oAuthAttributes);
        int defaultRequestCount = 0;

        // when
        Account newAccount = accountRepository.save(account);

        // then
        assertThat(newAccount.getRequestCount()).isNotNull();
        assertThat(newAccount.getRequestCount()).isEqualTo(defaultRequestCount);
    }

    @Test
    @DisplayName("Account 생성 시간 JPA Auditing 테스트")
    void created_date_time_of_account() {
        // given
        Account account = createMember();
        LocalDateTime testDateTime = LocalDateTime.now();

        // when
        Account newAccount = accountRepository.save(account);

        // then
        assertThat(newAccount.getCreatedDateTime()).isNotNull();
        assertThat(newAccount.getCreatedDateTime()).isAfter(testDateTime);
    }
}
