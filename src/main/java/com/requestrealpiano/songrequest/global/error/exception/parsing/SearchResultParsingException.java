package com.requestrealpiano.songrequest.global.error.exception.parsing;

import com.requestrealpiano.songrequest.global.error.exception.ParsingFailedException;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import lombok.Getter;

@Getter
public class SearchResultParsingException extends ParsingFailedException {

    public SearchResultParsingException() {
        super(ErrorCode.SEARCH_RESULT_ERROR);
    }
}
