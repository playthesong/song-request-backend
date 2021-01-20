package com.requestrealpiano.songrequest.domain.account;

import com.requestrealpiano.songrequest.domain.songrequest.SongRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "account")
    private List<SongRequest> songRequests = new ArrayList<>();

}
