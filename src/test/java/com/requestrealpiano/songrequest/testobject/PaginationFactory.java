package com.requestrealpiano.songrequest.testobject;

import com.requestrealpiano.songrequest.domain.letter.request.PaginationParameters;
import com.requestrealpiano.songrequest.global.pagination.response.PageDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static com.requestrealpiano.songrequest.global.constant.SortProperties.CREATED_DATE_TIME;

public class PaginationFactory {

    /*
     *
     * createMockObject()
     *   - Test parameter 에 의존하지 않는 테스트 객체 생성
     *     (ex. Mocking 에서 자주 사용되는 테스트 객체)
     *
     *
     * createMockObjectOf(T parameter1, T parameter2, ...)
     *   - Test parameter 에 의존하는 테스트 객체 생성
     *     (ex. 예외 검증, 경우의 수를 따져야 하는 테스트)
     *
     */

    // PaginationParameters
    public static PaginationParameters createPaginationParameters() {
        PaginationParameters parameters = new PaginationParameters();
        parameters.setPage(1);
        parameters.setSize(20);
        parameters.setDirection("ASC");
        return parameters;
    }

    public static PaginationParameters createPaginationParametersOf(Integer page, Integer size, String direction) {
        PaginationParameters parameters = new PaginationParameters();
        parameters.setPage(page);
        parameters.setSize(size);
        parameters.setDirection(direction);
        return parameters;
    }

    // PageRequest
    public static PageRequest createPageRequest() {
        PaginationParameters parameters = createPaginationParameters();
        Sort sortByCreatedDateTime = Sort.by(Sort.Direction.ASC, CREATED_DATE_TIME.getFieldName());
        return PageRequest.of(parameters.getPage(), parameters.getSize(), sortByCreatedDateTime);
    }

    // PageDetails
    public static <T> PageDetails createPageDetailsOf(Page<T> pageData) {
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
