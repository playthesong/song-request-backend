package com.requestrealpiano.songrequest.global.error;

import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;

@Getter
public class ErrorResponse {

    private final int statusCode;
    private final String message;
    private final List<ValueError> errors;

    private ErrorResponse(ErrorCode errorCode) {
        this.statusCode = errorCode.getStatusCode();
        this.message = errorCode.getMessage();
        this.errors = Collections.emptyList();
    }

    private ErrorResponse(ErrorCode errorCode, List<ValueError> errors) {
        this.statusCode = errorCode.getStatusCode();
        this.message = errorCode.getMessage();
        this.errors = errors;
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        return new ErrorResponse(errorCode, ValueError.from(bindingResult));
    }
}
