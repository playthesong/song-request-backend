package com.requestrealpiano.songrequest.global.error.exception.business;

import com.requestrealpiano.songrequest.global.error.exception.BusinessException;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
