package live.playthesong.songrequest.global.error.exception.business;

import live.playthesong.songrequest.global.error.response.ErrorCode;

public class LetterNotFoundException extends EntityNotFoundException {

    public LetterNotFoundException() {
        super(ErrorCode.LETTER_NOT_FOUND);
    }
}
