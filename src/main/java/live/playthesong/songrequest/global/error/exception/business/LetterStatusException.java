package live.playthesong.songrequest.global.error.exception.business;

import live.playthesong.songrequest.global.error.exception.BusinessException;
import live.playthesong.songrequest.global.error.response.ErrorCode;

public class LetterStatusException extends BusinessException {

    public LetterStatusException() { super(ErrorCode.INVALID_LETTER_STATUS); }
}
