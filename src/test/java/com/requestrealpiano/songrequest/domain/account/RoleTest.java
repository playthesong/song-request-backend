package com.requestrealpiano.songrequest.domain.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RoleTest {

    @ParameterizedTest
    @CsvSource({"ADMIN, MEMBER, GUEST, ROLE_ADMIN, ROLE_MEMBER, ROLE_GUEST, 관리자, 회원, 권한 승인 대기자"})
    @DisplayName("Role의 각 속성들을 테스트 한다.")
    void compare_role_properties(String adminName, String memberName, String guestName,
                                 String adminValue, String memberValue, String guestValue,
                                 String adminDescription, String memberDescription, String guestDescription) {
        // when
        Role admin = Role.ADMIN;
        Role member = Role.MEMBER;
        Role guest = Role.GUEST;

        // then
        assertAll(
                () -> assertThat(admin.getKey()).isEqualTo(adminName),
                () -> assertThat(admin.getValue()).isEqualTo(adminValue),
                () -> assertThat(admin.getDescription()).isEqualTo(adminDescription)
        );

        assertAll(
                () -> assertThat(member.getKey()).isEqualTo(memberName),
                () -> assertThat(member.getValue()).isEqualTo(memberValue),
                () -> assertThat(member.getDescription()).isEqualTo(memberDescription)
        );

        assertAll(
                () -> assertThat(guest.getKey()).isEqualTo(guestName),
                () -> assertThat(guest.getValue()).isEqualTo(guestValue),
                () -> assertThat(guest.getDescription()).isEqualTo(guestDescription)
        );

    }
}
