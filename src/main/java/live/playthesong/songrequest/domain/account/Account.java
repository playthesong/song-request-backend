package live.playthesong.songrequest.domain.account;

import live.playthesong.songrequest.domain.base.BaseTimeEntity;
import live.playthesong.songrequest.domain.letter.Letter;
import live.playthesong.songrequest.security.oauth.OAuthAttributes;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private Account(Long id, String googleOauthId, String name, String email, Role role, String avatarUrl,
                    Integer requestCount) {
        this.id = id;
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

    public Account increaseRequestCount() {
        // Letter 등록 때마다 1 증가한다.
        this.requestCount += 1;
        return this;
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
                      .build();
    }

    @PrePersist
    public void prePersist() {
        // 0 - Account 데이터 생성시 RequestCount 기본 값
        if (this.requestCount == null) {
            this.requestCount = 0;
        }
    }
}
