package com.requestrealpiano.songrequest.global.error.exception.jwt;

import com.requestrealpiano.songrequest.global.error.exception.JwtValidationException;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;

public class JwtRequiredException extends JwtValidationException {

    public JwtRequiredException() { super(ErrorCode.UNAUTHENTICATED_ERROR); }
}
