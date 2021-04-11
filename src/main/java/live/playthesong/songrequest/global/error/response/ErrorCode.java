package live.playthesong.songrequest.global.error.response;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "입력한 요청 값이 올바르지 않습니다. 다시 요청 해주세요."),
    INVALID_REQUEST_ERROR(400, "올바른 요청이 아닙니다."),
    SEARCH_RESULT_ERROR(500, "검색 결과를 가져오지 못했습니다. 다시 요청 해주세요."),
    METHOD_NOT_ALLOWED(405, "지원하지 않는 요청 메소드 입니다."),
    INTERNAL_SERVER_ERROR(500, "서버에서 요청을 처리하지 못했습니다."),

    // Auth
    UNAUTHENTICATED_ERROR(401, "인증이 필요합니다. 로그인 이후 다시 시도 해주세요."),
    JWT_INVALID_ERROR(400, "올바른 인증 정보가 아닙니다. 다시 로그인 해주세요."),
    JWT_EXPIRATION_ERROR(401, "인증 정보가 만료 되었습니다. 다시 로그인 해주세요."),
    ACCESS_DENIED_ERROR(403, "해당 요청에 대한 접근 권한이 없습니다."),

    // Account
    ACCOUNT_MISMATCH_ERROR(400, "등록된 정보와 요청한 계정 정보와 일치하지 않습니다."),
    ACCOUNT_NOT_FOUND(404, "해당 계정이 존재하지 않습니다."),

    // Letter
    INVALID_LETTER_STATUS(400, "요청한 신청곡 상태 변경 정보가 유효하지 않습니다."),
    LETTER_NOT_READY(400, "현재 신청곡 등록이 중단되어 있습니다. 나중에 다시 시도 해주세요."),
    LETTER_NOT_FOUND(404, "해당 신청곡 및 사연이 존재하지 않습니다."),

    // Song
    SONG_NOT_FOUND(404, "해당 음원 데이터가 존재하지 않습니다.")

    ;

    private final int statusCode;
    private final String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
