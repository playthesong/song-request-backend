package live.playthesong.songrequest.global.error.exception.jwt;

import live.playthesong.songrequest.global.error.exception.JwtValidationException;
import live.playthesong.songrequest.global.error.response.ErrorCode;

public class JwtExpirationException extends JwtValidationException {

    public JwtExpirationException() {
        super(ErrorCode.JWT_EXPIRATION_ERROR);
    }
}
