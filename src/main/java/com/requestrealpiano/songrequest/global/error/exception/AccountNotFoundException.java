package com.requestrealpiano.songrequest.global.error.exception;

import com.requestrealpiano.songrequest.global.error.ErrorCode;

public class AccountNotFoundException extends EntityNotFoundException {

    public AccountNotFoundException() {
        super(ErrorCode.ACCOUNT_NOT_FOUND);
    }
}
