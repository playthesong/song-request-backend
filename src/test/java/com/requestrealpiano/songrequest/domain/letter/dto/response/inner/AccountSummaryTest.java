package com.requestrealpiano.songrequest.domain.letter.dto.response.inner;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AccountSummaryTest {

    @ParameterizedTest
    @MethodSource("createNewAccountSummaryParameters")
    @DisplayName("Account 객체로부터 AccountSummary DTO를 생성하는 테스트")
    void create_new_account_summary(Long googleOauthId, String name, String email, Role role, String avatarUrl,
                                    Integer requestCount) {
        // given
        Account account = Account.builder()
                                 .googleOauthId(googleOauthId)
                                 .name(name)
                                 .email(email)
                                 .role(role)
                                 .avatarUrl(avatarUrl)
                                 .requestCount(requestCount)
                                 .build();

        // when
        AccountSummary accountSummary = AccountSummary.from(account);

        // then
        assertThat(accountSummary.getName()).isEqualTo(name);
        assertThat(accountSummary.getAvatarUrl()).isEqualTo(avatarUrl);
    }

    private static Stream<Arguments> createNewAccountSummaryParameters() {
        return Stream.of(
                Arguments.of(1234567L, "Account Name 1", "first@email.com", Role.ADMIN, "http://avatarUrl_1", 3),
                Arguments.of(234567L, "Account Name 2", "second@email.com", Role.MEMBER, "http://avatarUrl_2", 5),
                Arguments.of(34567L, "Account Name 3", "third@email.com", Role.GUEST, "http://avatarUrl_3", 7)
        );
    }
}
