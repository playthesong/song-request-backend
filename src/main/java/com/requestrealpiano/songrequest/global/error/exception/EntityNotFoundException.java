package com.requestrealpiano.songrequest.global.error.exception;

import com.requestrealpiano.songrequest.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
