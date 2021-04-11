package live.playthesong.songrequest.global.error.exception.jwt;

import live.playthesong.songrequest.global.error.exception.JwtValidationException;
import live.playthesong.songrequest.global.error.response.ErrorCode;

public class JwtRequiredException extends JwtValidationException {

    public JwtRequiredException() { super(ErrorCode.UNAUTHENTICATED_ERROR); }
}
