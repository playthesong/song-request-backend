package com.requestrealpiano.songrequest.domain.letter.dto.response.inner;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.testobject.AccountFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountSummaryTest {

    @Test
    @DisplayName("Account 객체로부터 AccountSummary DTO를 생성하는 테스트")
    void create_new_account_summary() {
        // given
        Account account = AccountFactory.createMember();

        // when
        AccountSummary accountSummary = AccountSummary.from(account);

        // then
        assertThat(accountSummary.getName()).isEqualTo(account.getName());
        assertThat(accountSummary.getAvatarUrl()).isEqualTo(account.getAvatarUrl());
    }
}
