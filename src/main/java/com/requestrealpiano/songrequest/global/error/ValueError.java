package com.requestrealpiano.songrequest.global.error;

import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ValueError {
    private final String value;
    private final String reason;

    private ValueError(String value, String reason) {
        this.value = value;
        this.reason = reason;
    }

    public static List<ValueError> from(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        return errors.stream()
                     .map(error -> new ValueError(error.getField(), error.getDefaultMessage()))
                     .collect(Collectors.toList());
    }
}
