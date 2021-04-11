package live.playthesong.songrequest.global.error.exception.business;

import live.playthesong.songrequest.global.error.exception.BusinessException;
import live.playthesong.songrequest.global.error.response.ErrorCode;

public class AccountMismatchException extends BusinessException {

    public AccountMismatchException() { super(ErrorCode.ACCOUNT_MISMATCH_ERROR); }
}
