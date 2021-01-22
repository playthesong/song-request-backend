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

    public static <T> ApiResponse<T> CREATED() {
        return new ApiResponse<>(true, "CREATED", null);
    }

    public static <T> ApiResponse<T> UPDATED() {
        return new ApiResponse<>(true, "UPDATED", null);
    }

    public static <T> ApiResponse<T> DELETED() {
        return new ApiResponse<>(true, "DELETED", null);
    }
}
