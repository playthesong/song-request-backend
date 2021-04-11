package live.playthesong.songrequest.global.error.exception.parsing;

import live.playthesong.songrequest.global.error.exception.ParsingFailedException;
import live.playthesong.songrequest.global.error.response.ErrorCode;
import lombok.Getter;

@Getter
public class SearchResultParsingException extends ParsingFailedException {

    public SearchResultParsingException() {
        super(ErrorCode.SEARCH_RESULT_ERROR);
    }
}
