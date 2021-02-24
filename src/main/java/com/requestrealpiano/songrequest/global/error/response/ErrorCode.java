package com.requestrealpiano.songrequest.global.error.response;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "입력한 요청 값이 올바르지 않습니다. 다시 요청 해주세요."),
    SEARCH_RESULT_ERROR(500, "검색 결과를 가져오지 못했습니다. 다시 요청 해주세요."),
    METHOD_NOT_ALLOWED(405, "지원하지 않는 요청 메소드 입니다."),
    INTERNAL_SERVER_ERROR(500, "서버에서 요청을 처리하지 못했습니다."),

    // Account
    ACCOUNT_NOT_FOUND(404, "해당 계정이 존재하지 않습니다."),

    // Letter
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
