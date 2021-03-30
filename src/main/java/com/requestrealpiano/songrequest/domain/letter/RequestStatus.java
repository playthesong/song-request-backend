package com.requestrealpiano.songrequest.domain.letter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.requestrealpiano.songrequest.global.error.exception.business.LetterStatusException;

import java.util.stream.Stream;

public enum RequestStatus {
    WAITING,
    DONE,
    PENDING
    ;

    public String getKey() {
        return name();
    }

    @JsonCreator
    public static RequestStatus createJson(String requestStatus) {
        return Stream.of(values())
                     .filter(status -> status.getKey().equalsIgnoreCase(requestStatus))
                     .findFirst()
                     .orElse(null);
    }
}
