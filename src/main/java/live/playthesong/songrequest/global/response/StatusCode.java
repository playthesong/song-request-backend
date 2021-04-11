package live.playthesong.songrequest.global.response;

import lombok.Getter;

@Getter
public enum StatusCode {
    OK(200),
    CREATED(201),
    NO_CONTENT(204)

    ;

    private final int code;

    StatusCode(int code) {
        this.code = code;
    }
}
