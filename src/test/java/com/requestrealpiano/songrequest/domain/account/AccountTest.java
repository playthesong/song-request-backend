package com.requestrealpiano.songrequest.domain.account;

import org.junit.jupiter.api.Test;

import static com.requestrealpiano.songrequest.testobject.AccountFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @Test
    void increase_request_count() {
        // given
        int increase = 1;
        Account account = createMember();
        int beforeCount = account.getRequestCount();

        // when
        Account updatedAccount = account.increaseRequestCount();
        int afterCount = updatedAccount.getRequestCount();

        // then
        assertThat(afterCount).isEqualTo(beforeCount + increase);
    }
}
