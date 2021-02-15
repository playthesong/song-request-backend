package com.requestrealpiano.songrequest.testobject;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.Role;

public class AccountFactory {

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
