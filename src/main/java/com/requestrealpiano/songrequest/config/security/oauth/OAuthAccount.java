package com.requestrealpiano.songrequest.config.security.oauth;

import com.requestrealpiano.songrequest.domain.account.Account;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Getter
public class OAuthAccount implements OAuth2User {

    private final Map<String, Object> attributes;
    private final Set<SimpleGrantedAuthority> authorities;
    private final Long id;
    private final String googleOAuthId;
    private final String name;
    private final String email;
    private final String avatarUrl;
    private final Integer requestCount;

    @Builder
    private OAuthAccount(Map<String, Object> attributes, Set<SimpleGrantedAuthority> authorities, Long id,
                         String googleOAuthId, String name, String email, String avatarUrl, Integer requestCount) {
        this.attributes = attributes;
        this.authorities = authorities;
        this.id = id;
        this.googleOAuthId = googleOAuthId;
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.requestCount = requestCount;
    }

    public static OAuthAccount of(Map<String, Object> attributes, Account account) {
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(account.getRoleKey()));

        return OAuthAccount.builder()
                           .attributes(attributes)
                           .authorities(authorities)
                           .id(account.getId())
                           .googleOAuthId(account.getGoogleOauthId())
                           .name(account.getName())
                           .email(account.getEmail())
                           .avatarUrl(account.getAvatarUrl())
                           .requestCount(account.getRequestCount())
                           .build();
    }
}
