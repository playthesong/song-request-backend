package com.requestrealpiano.songrequest.global.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final boolean success;
    private final String statusMessage;
    private final T data;

    private ApiResponse(boolean success, String statusMessage, T data) {
        this.success = success;
        this.statusMessage = statusMessage;
        this.data = data;
    }

    public static <T> ApiResponse<T> OK(T data) {
        return new ApiResponse<>(true, "OK", data);
    }
}
