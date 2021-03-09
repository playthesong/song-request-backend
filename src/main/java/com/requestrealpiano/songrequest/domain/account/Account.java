package com.requestrealpiano.songrequest.domain.account;

import com.requestrealpiano.songrequest.config.security.oauth.OAuthAttributes;
import com.requestrealpiano.songrequest.domain.base.BaseTimeEntity;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "google_oauth_id")
    private String googleOauthId;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "request_count")
    private Integer requestCount;

    @OneToMany(mappedBy = "account")
    private List<Letter> letters = new ArrayList<>();

    @Builder
    private Account(String googleOauthId, String name, String email, Role role, String avatarUrl, Integer requestCount) {
        this.googleOauthId = googleOauthId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.avatarUrl = avatarUrl;
        this.requestCount = requestCount;
    }

    public String getRoleKey() { return role.getKey(); }

    public String getRoleValue() {
        return role.getValue();
    }

    public Account updateProfile(OAuthAttributes attributes) {
        this.name = attributes.getName();
        this.email = attributes.getEmail();
        this.avatarUrl = attributes.getAvatarUrl();
        return this;
    }

    public static Account from(OAuthAttributes oAuthAttributes) {
        return Account.builder()
                      .googleOauthId(oAuthAttributes.getGoogleOauthId())
                      .name(oAuthAttributes.getName())
                      .email(oAuthAttributes.getEmail())
                      .role(Role.MEMBER)
                      .avatarUrl(oAuthAttributes.getAvatarUrl())
                      .requestCount(0)
                      .build();
    }
}
