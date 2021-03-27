package com.requestrealpiano.songrequest.global.pagination.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageDetails {

    private final int totalPages;
    private final int currentPage;
    private final long totalDataCount;
    private final int currentCount;
    private final boolean firstPage;
    private final boolean lastPage;

    @Builder
    private PageDetails(int totalPages, int currentPage, long totalDataCount, int currentCount,
                        boolean firstPage, boolean lastPage) {
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.totalDataCount = totalDataCount;
        this.currentCount = currentCount;
        this.firstPage = firstPage;
        this.lastPage = lastPage;
    }

    public static <T> PageDetails from(Page<T> pageData) {
        return PageDetails.builder()
                          .totalPages(pageData.getTotalPages())
                          .currentPage(pageData.getNumber())
                          .totalDataCount(pageData.getTotalElements())
                          .currentCount(pageData.getNumberOfElements())
                          .firstPage(pageData.isFirst())
                          .lastPage(pageData.isLast())
                          .build();
    }
}
