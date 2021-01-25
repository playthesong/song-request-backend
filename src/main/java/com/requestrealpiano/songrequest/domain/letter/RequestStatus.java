package com.requestrealpiano.songrequest.domain.letter;

public enum RequestStatus {
    WAITING("대기"),
    DONE("완료"),
    PENDING("보류")
    ;

    private final String value;

    RequestStatus(String value) {
        this.value = value;
    }

    public String getKey() {
        return name();
    }

    public String getStatus() {
        return value;
    }
}
