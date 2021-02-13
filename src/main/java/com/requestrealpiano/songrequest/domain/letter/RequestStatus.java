package com.requestrealpiano.songrequest.domain.letter;

public enum RequestStatus {
    WAITING,
    DONE,
    PENDING
    ;

    public String getKey() {
        return name();
    }
}
