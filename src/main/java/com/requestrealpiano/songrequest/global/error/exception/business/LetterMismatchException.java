package com.requestrealpiano.songrequest.global.error.exception.business;

import com.requestrealpiano.songrequest.global.error.exception.BusinessException;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;

public class LetterMismatchException extends BusinessException {

    public LetterMismatchException() { super(ErrorCode.ACCOUNT_MISMATCH_ERROR); }
}
