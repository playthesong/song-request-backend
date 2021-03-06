package live.playthesong.songrequest.domain.letter.request;

import live.playthesong.songrequest.domain.letter.RequestStatus;

public class StatusChangeRequestBuilder {

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private RequestStatus requestStatus;

        public Builder requestStatus(RequestStatus requestStatus) {
            this.requestStatus = requestStatus;
            return this;
        }

        public StatusChangeRequest build() {
            return new StatusChangeRequest(requestStatus);
        }
    }
}
