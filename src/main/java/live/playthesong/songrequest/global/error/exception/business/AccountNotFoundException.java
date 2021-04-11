package live.playthesong.songrequest.global.error.exception.business;

import live.playthesong.songrequest.global.error.response.ErrorCode;

public class AccountNotFoundException extends EntityNotFoundException {

    public AccountNotFoundException() {
        super(ErrorCode.ACCOUNT_NOT_FOUND);
    }
}
