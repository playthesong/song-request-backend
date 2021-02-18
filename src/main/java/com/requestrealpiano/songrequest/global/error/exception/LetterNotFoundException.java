package com.requestrealpiano.songrequest.global.error.exception;

import com.requestrealpiano.songrequest.global.error.response.ErrorCode;

public class LetterNotFoundException extends EntityNotFoundException {

    public LetterNotFoundException() {
        super(ErrorCode.LETTER_NOT_FOUND);
    }
}
