package com.requestrealpiano.songrequest.domain.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AccountDetail {

    private final Long id;
    private final String name;
    private final String email;
    private final String avatarUrl;
    private final String role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy / MM / dd")
    private final LocalDateTime createdDateTime;

    private final Integer requestCount;

    @Builder
    private AccountDetail(Long id, String name, String email, String avatarUrl,
                         String role, LocalDateTime createdDateTime, Integer requestCount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.role = role;
        this.createdDateTime = createdDateTime;
        this.requestCount = requestCount;
    }

    public static AccountDetail from(Account account) {
        return AccountDetail.builder()
                            .id(account.getId())
                            .name(account.getName())
                            .email(account.getEmail())
                            .avatarUrl(account.getAvatarUrl())
                            .role(account.getRoleKey())
                            .createdDateTime(account.getCreatedDateTime())
                            .requestCount(account.getRequestCount())
                            .build();
    }
}
