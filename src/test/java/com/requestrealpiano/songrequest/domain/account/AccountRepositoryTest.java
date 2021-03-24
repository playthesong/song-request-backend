package com.requestrealpiano.songrequest.domain.account;

import com.requestrealpiano.songrequest.testconfig.BaseRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.requestrealpiano.songrequest.testobject.AccountFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;

class AccountRepositoryTest extends BaseRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    @DisplayName("Account 생성 시 RequestCount 기본 값 설정 테스트")
    void create_account_default_request_count() {
        // given
        Account account = createMember();
        int defaultRequestCount = 0;

        // when
        Account newAccount = accountRepository.save(account);

        // then
        assertThat(newAccount.getRequestCount()).isNotNull();
        assertThat(newAccount.getRequestCount()).isEqualTo(defaultRequestCount);
    }
}
