package live.playthesong.songrequest.domain.letter.request;

import live.playthesong.songrequest.domain.letter.RequestStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StatusChangeRequest {

    private RequestStatus requestStatus;

    StatusChangeRequest(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}
