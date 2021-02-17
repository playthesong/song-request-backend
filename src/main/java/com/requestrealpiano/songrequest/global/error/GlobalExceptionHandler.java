package com.requestrealpiano.songrequest.global.error;

import com.requestrealpiano.songrequest.global.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        log.error("handleMethodArgumentNotValidException", exception);
        ErrorResponse errorResponse = ErrorResponse.from(ErrorCode.INVALID_INPUT_VALUE);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        log.error("handleBusinessException", exception);
        ErrorCode errorCode = exception.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.from(errorCode);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }
}
