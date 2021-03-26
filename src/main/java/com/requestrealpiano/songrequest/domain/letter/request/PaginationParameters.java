package com.requestrealpiano.songrequest.domain.letter.request;

import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static com.requestrealpiano.songrequest.global.constant.ValidationCondition.*;

@Getter
public class PaginationParameters {

    @Min(value = PAGE_MIN, message = PAGE_MESSAGE)
    private Integer page;

    @Min(value = PAGE_SIZE_MIN, message = PAGE_SIZE_MESSAGE)
    @Max(value = PAGE_SIZE_MAX, message = PAGE_SIZE_MESSAGE)
    private Integer size;

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
}
