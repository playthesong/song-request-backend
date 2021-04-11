package live.playthesong.songrequest.domain.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static live.playthesong.songrequest.testobject.AccountFactory.createMemberOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AccountDetailTest {

    @Test
    @DisplayName("Account로부터 AccountDetail 을 생성하는 테스트")
    void accountdetail_from_account() {
        // given
        Account account = createMemberOf(1L);

        // when
        AccountDetail accountDetail = AccountDetail.from(account);

        // then
        assertAll(
                () -> assertThat(accountDetail.getId()).isEqualTo(account.getId()),
                () -> assertThat(accountDetail.getName()).isEqualTo(account.getName()),
                () -> assertThat(accountDetail.getEmail()).isEqualTo(account.getEmail()),
                () -> assertThat(accountDetail.getAvatarUrl()).isEqualTo(account.getAvatarUrl()),
                () -> assertThat(accountDetail.getRole()).isEqualTo(account.getRoleKey()),
                () -> assertThat(accountDetail.getCreatedDateTime()).isEqualTo(account.getCreatedDateTime()),
                () -> assertThat(accountDetail.getRequestCount()).isEqualTo(account.getRequestCount())
        );
    }
}
