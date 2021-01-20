package com.requestrealpiano.songrequest.domain.account;

public enum Role {
    ADMIN("ROLE_ADMIN", "관리자"),
    MEMBER("ROLE_MEMBER", "회원"),
    GUEST("ROLE_GUEST", "권한 승인 대기자")
    ;

    private final String value;
    private final String description;

    private Role(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
