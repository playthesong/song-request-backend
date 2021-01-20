package com.requestrealpiano.songrequest.domain.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AccountTest {

    @ParameterizedTest
    @MethodSource("createNewAccountParameters")
    @DisplayName("새로운 Account를 생성할 수 있다.")
    void create_new_account(Long googleOauthId, String name, String email, Role role, String avatarUrl,
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
        // then
        assertAll(
                () -> assertThat(account).isNotNull(),
                () -> assertThat(account.getGoogleOauthId()).isEqualTo(googleOauthId),
                () -> assertThat(account.getName()).isEqualTo(name),
                () -> assertThat(account.getEmail()).isEqualTo(email),
                () -> assertThat(account.getRole()).isEqualTo(role),
                () -> assertThat(account.getAvatarUrl()).isEqualTo(avatarUrl),
                () -> assertThat(account.getRequestCount()).isEqualTo(requestCount),
                () -> assertThat(account.getCreatedDate()).isNotNull(),
                () -> assertThat(account.getLetters()).isNotNull()
        );

    }

    private static Stream<Arguments> createNewAccountParameters() {
        return Stream.of(
                Arguments.of(12345L, "First Account", "first@email.com", Role.ADMIN, "http://avatarUrl_1", 5),
                Arguments.of(23456L, "Second Account", "second@email.com", Role.MEMBER, "http://avatarUrl_2", 6),
                Arguments.of(3456789L, "Third Account", "third@email.com", Role.GUEST, "http://avatarUrl_3", 7)
        );
    }
}
