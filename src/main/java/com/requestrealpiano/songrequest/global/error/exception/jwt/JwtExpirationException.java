package com.requestrealpiano.songrequest.global.error.exception.jwt;

import com.requestrealpiano.songrequest.global.error.exception.JwtValidationException;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;

public class JwtExpirationException extends JwtValidationException {

    public JwtExpirationException() {
        super(ErrorCode.JWT_EXPIRATION_ERROR);
    }
}
