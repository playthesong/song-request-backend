package live.playthesong.songrequest.global.pagination;

import live.playthesong.songrequest.global.constant.SortProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class Pagination {

    public static PageRequest of(int page, int size, String direction, SortProperties property) {
        Sort sort = Sort.by(Direction.fromString(direction), property.getFieldName());
        return PageRequest.of(page, size, sort);
    }
}
