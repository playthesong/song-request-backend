package com.requestrealpiano.songrequest.global.error.exception.business;

import com.requestrealpiano.songrequest.global.error.exception.BusinessException;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;

public class LetterStatusException extends BusinessException {

    public LetterStatusException() { super(ErrorCode.INVALID_LETTER_STATUS); }
}
