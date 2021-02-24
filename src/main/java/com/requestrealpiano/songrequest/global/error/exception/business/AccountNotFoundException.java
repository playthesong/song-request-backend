package com.requestrealpiano.songrequest.global.error.exception.business;

import com.requestrealpiano.songrequest.global.error.response.ErrorCode;

public class AccountNotFoundException extends EntityNotFoundException {

    public AccountNotFoundException() {
        super(ErrorCode.ACCOUNT_NOT_FOUND);
    }
}
