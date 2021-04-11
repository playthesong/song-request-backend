package live.playthesong.songrequest.global.error.exception.jwt;

import live.playthesong.songrequest.global.error.exception.JwtValidationException;
import live.playthesong.songrequest.global.error.response.ErrorCode;

public class JwtInvalidTokenException extends JwtValidationException {

    public JwtInvalidTokenException() {
        super(ErrorCode.JWT_INVALID_ERROR);
    }
}
