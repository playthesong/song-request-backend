package com.requestrealpiano.songrequest.testobject;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.Role;

public class AccountFactory {

    /*
     *
     * createMockObject()
     *   - Test parameter 에 의존하지 않는 테스트 객체 생성
     *     (ex. Mocking 에서 자주 사용되는 테스트 객체)
     *
     *
     * createMockObjectOf(T parameter1, T parameter2, ...)
     *   - Test parameter 에 의존하는 테스트 객체 생성
     *     (ex. 예외 검증, 경우의 수를 따져야 하는 테스트)
     */

    // Role - Member
    public static Account createMember() {
        return Account.builder()
                      .googleOauthId(1L)
                      .name("Username")
                      .email("User email")
                      .role(Role.MEMBER)
                      .avatarUrl("http://avatarUrl")
                      .requestCount(1)
                      .build();
    }
}
