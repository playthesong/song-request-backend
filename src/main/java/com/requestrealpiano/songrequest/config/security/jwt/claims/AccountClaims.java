package com.requestrealpiano.songrequest.config.security.jwt.claims;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.Role;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountClaims {

    private final Long id;
    private final String name;
    private final String email;
    private final String avatarUrl;
    private final String role;

    @Builder
    private AccountClaims(Long id, String name, String email, String avatarUrl, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.role = role;
    }

    public static AccountClaims from(Account account) {
        Role role = account.getRole();

        return AccountClaims.builder()
                            .id(account.getId())
                            .name(account.getName())
                            .email(account.getEmail())
                            .avatarUrl(account.getAvatarUrl())
                            .role(role.getKey())
                            .build();
    }

    public static AccountClaims from(Claims claims) {
        return AccountClaims.builder()
                            .id(claims.get("id", Long.class))
                            .name(claims.get("name", String.class))
                            .email(claims.get("email", String.class))
                            .avatarUrl(claims.get("avatarUrl", String.class))
                            .role(claims.get("role", String.class))
                            .build();
    }
}
