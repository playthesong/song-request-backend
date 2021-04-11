package live.playthesong.songrequest.global.constant;

import lombok.Getter;

@Getter
public enum SortProperties {
    CREATED_DATE_TIME("createdDateTime")

    ;

    private final String fieldName;

    SortProperties(String fieldName) {
        this.fieldName = fieldName;
    }
}
