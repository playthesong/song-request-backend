package live.playthesong.songrequest.global.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final int statusCode;
    private final T data;

    private ApiResponse(int statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public static <T> ApiResponse<T> SUCCESS(StatusCode statusCode, T data) {
        return new ApiResponse<>(statusCode.getCode(), data);
    }
}
