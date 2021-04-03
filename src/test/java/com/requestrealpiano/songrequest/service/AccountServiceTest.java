package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountDetail;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.domain.account.Role;
import com.requestrealpiano.songrequest.security.oauth.OAuthAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.requestrealpiano.songrequest.domain.account.Role.MEMBER;
import static com.requestrealpiano.songrequest.testobject.AccountFactory.createMemberOf;
import static com.requestrealpiano.songrequest.testobject.AccountFactory.createOAuthAccountOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    AccountService accountService;

    @Mock
    AccountRepository accountRepository;

    @Test
    @DisplayName("Account 를 조회한 뒤 AccountDetails로 반환하는 테스트")
    void find_account_detail() {
        // given
        OAuthAccount loginAccount = createOAuthAccountOf(MEMBER);
        Account account = createMemberOf(loginAccount.getId());

        // when
        when(accountRepository.findById(eq(account.getId()))).thenReturn(Optional.of(account));
        AccountDetail accountDetail = accountService.findAccountDetail(loginAccount);

        // then
        assertAll(
                () -> assertThat(accountDetail.getId()).isEqualTo(account.getId()),
                () -> assertThat(accountDetail.getEmail()).isEqualTo(account.getEmail()),
                () -> assertThat(accountDetail.getAvatarUrl()).isEqualTo(account.getAvatarUrl())
        );
    }
}
