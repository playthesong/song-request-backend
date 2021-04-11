package live.playthesong.songrequest.global.error.exception.business;

import live.playthesong.songrequest.global.error.exception.BusinessException;
import live.playthesong.songrequest.global.error.response.ErrorCode;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
