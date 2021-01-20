package com.requestrealpiano.songrequest.domain.songrequest.dto.response.inner;

import com.requestrealpiano.songrequest.domain.account.Account;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountSummary {

    private final Long id;
    private final String name;
    private final String avatarUrl;

    @Builder
    private AccountSummary(Long id, String name, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public static AccountSummary from(Account account) {
        return AccountSummary.builder()
                             .id(account.getId())
                             .name(account.getName())
                             .avatarUrl(account.getAvatarUrl())
                             .build();
    }
}
