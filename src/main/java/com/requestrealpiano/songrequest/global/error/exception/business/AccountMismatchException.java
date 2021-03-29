package com.requestrealpiano.songrequest.global.error.exception.business;

import com.requestrealpiano.songrequest.global.error.exception.BusinessException;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;

public class AccountMismatchException extends BusinessException {

    public AccountMismatchException() { super(ErrorCode.ACCOUNT_MISMATCH_ERROR); }
}
