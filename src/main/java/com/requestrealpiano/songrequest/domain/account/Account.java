package com.requestrealpiano.songrequest.domain.account;

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
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "google_oauth_id")
    private Long googleOauthId;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "reqeust_count")
    private Integer requestCount;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @OneToMany(mappedBy = "account")
    private List<Letter> letters = new ArrayList<>();

    @Builder
    private Account(Long googleOauthId, String name, String email, Role role, String avatarUrl, Integer requestCount) {
        this.googleOauthId = googleOauthId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.avatarUrl = avatarUrl;
        this.requestCount = requestCount;
        this.createdDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
    }
}
