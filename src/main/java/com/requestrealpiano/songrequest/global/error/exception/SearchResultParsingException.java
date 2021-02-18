package com.requestrealpiano.songrequest.global.error.exception;

import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import lombok.Getter;

@Getter
public class SearchResultParsingException extends ParsingFailedException {

    public SearchResultParsingException() {
        super(ErrorCode.SEARCH_RESULT_ERROR);
    }
}
