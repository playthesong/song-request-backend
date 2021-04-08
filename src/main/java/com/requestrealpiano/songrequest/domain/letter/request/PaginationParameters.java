package com.requestrealpiano.songrequest.domain.letter.request;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static com.requestrealpiano.songrequest.global.constant.ValidationCondition.*;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Getter
public class PaginationParameters {

    @Min(value = PAGE_MIN, message = PAGE_MESSAGE)
    private Integer page;

    @Min(value = PAGE_SIZE_MIN, message = PAGE_SIZE_MESSAGE)
    @Max(value = PAGE_SIZE_MAX, message = PAGE_SIZE_MESSAGE)
    private Integer size;

    private String direction;

    public void setPage(Integer page) {
        if (page == null|| page <= PAGE_MIN) {
            this.page = PAGE_MIN;
            return;
        }
        this.page = page - 1;
    }

    public void setSize(Integer size) {
        if (size == null || size < PAGE_SIZE_MIN || size > PAGE_SIZE_MAX) {
            this.size = PAGE_SIZE_DEFAULT;
            return;
        }
        this.size = size;
    }

    public void setDirection(String direction) {
        if (StringUtils.isEmpty(direction)) {
            this.direction = DESC.name();
            return;
        }

        if (StringUtils.equalsAnyIgnoreCase(direction, ASC.name(), DESC.name())) {
            this.direction = direction;
            return;
        }
        this.direction = DESC.name();
    }
}
