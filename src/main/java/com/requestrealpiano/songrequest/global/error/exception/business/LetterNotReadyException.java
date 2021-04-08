package com.requestrealpiano.songrequest.global.error.exception.business;

import com.requestrealpiano.songrequest.global.error.exception.BusinessException;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;

public class LetterNotReadyException extends BusinessException {

    public LetterNotReadyException() {
        super(ErrorCode.LETTER_NOT_READY);
    }
}
