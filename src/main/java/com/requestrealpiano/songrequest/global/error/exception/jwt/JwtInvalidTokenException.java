package com.requestrealpiano.songrequest.global.error.exception.jwt;

import com.requestrealpiano.songrequest.global.error.exception.JwtValidationException;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;

public class JwtInvalidTokenException extends JwtValidationException {

    public JwtInvalidTokenException() {
        super(ErrorCode.JWT_INVALID_ERROR);
    }
}
