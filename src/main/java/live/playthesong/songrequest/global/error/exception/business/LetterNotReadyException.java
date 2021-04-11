package live.playthesong.songrequest.global.error.exception.business;

import live.playthesong.songrequest.global.error.exception.BusinessException;
import live.playthesong.songrequest.global.error.response.ErrorCode;

public class LetterNotReadyException extends BusinessException {

    public LetterNotReadyException() {
        super(ErrorCode.LETTER_NOT_READY);
    }
}
