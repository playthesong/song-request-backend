package com.requestrealpiano.songrequest.global.error.exception;

import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import lombok.Getter;

@Getter
public class JwtValidationException extends RuntimeException {

    private final ErrorCode errorCode;

    public JwtValidationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
