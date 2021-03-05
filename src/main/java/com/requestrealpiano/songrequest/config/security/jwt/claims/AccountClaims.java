package com.requestrealpiano.songrequest.config.security.jwt.claims;

import com.requestrealpiano.songrequest.config.security.oauth.OAuthAccount;
import com.requestrealpiano.songrequest.domain.account.Account;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountClaims {

    private final long id;
    private final String name;
    private final String email;
    private final String avatarUrl;

    @Builder
    private AccountClaims(long id, String name, String email, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

    public static AccountClaims from(Account account) {
        return AccountClaims.builder()
                            .id(account.getId())
                            .name(account.getName())
                            .email(account.getEmail())
                            .avatarUrl(account.getAvatarUrl())
                            .build();
    }

    public static AccountClaims from(OAuthAccount oAuthAccount) {
        return AccountClaims.builder()
                            .id(oAuthAccount.getId())
                            .name(oAuthAccount.getName())
                            .email(oAuthAccount.getEmail())
                            .avatarUrl(oAuthAccount.getAvatarUrl())
                            .build();
    }
}
