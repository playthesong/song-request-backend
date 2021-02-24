package com.requestrealpiano.songrequest.global.error.exception;

import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import lombok.Getter;

@Getter
public class ParsingFailedException extends RuntimeException {

    private final ErrorCode errorCode;

    public ParsingFailedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
