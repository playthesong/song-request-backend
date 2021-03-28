package com.requestrealpiano.songrequest.testobject;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.Role;
import com.requestrealpiano.songrequest.security.oauth.OAuthAccount;
import com.requestrealpiano.songrequest.security.oauth.OAuthAttributes;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

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
     *
     */

    // Role - Member
    public static Account createMember() {
        return Account.builder()
                      .googleOauthId("771713471123")
                      .name("Username")
                      .email("User email")
                      .role(Role.MEMBER)
                      .avatarUrl("http://avatarUrl")
                      .build();
    }

    public static Account createMemberOf(OAuthAttributes oAuthAttributes) {
        return Account.from(oAuthAttributes);
    }

    // OAuthAccount
    public static OAuthAccount createOAuthAccountOf(Role role) {
        return OAuthAccount.builder()
                           .id(1L)
                           .authorities(Collections.singleton(new SimpleGrantedAuthority(role.getValue())))
                           .build();
    }

    public static OAuthAccount createOAuthAccountOf(Long id, Role role) {
        return OAuthAccount.builder()
                           .id(id)
                           .authorities(Collections.singleton(new SimpleGrantedAuthority(role.getValue())))
                           .build();
    }

    // OAuthAttributes
    public static OAuthAttributes createOAuthAttributes() {
        return OAuthAttributes.builder()
                              .googleOauthId("777177171")
                              .name("Default Attribute Name")
                              .email("Default Attribute Email")
                              .avatarUrl("Default AvatarUrl")
                              .build();
    }
}
