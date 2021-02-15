package com.requestrealpiano.songrequest.domain.letter.dto.request;

import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequest;

public class NewLetterRequestBuilder {

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String songStory;
        private SongRequest songRequest;
        private Long accountId;

        public Builder songStory(String songStory) {
            this.songStory = songStory;
            return this;
        }

        public Builder songRequest(SongRequest song) {
            this.songRequest = song;
            return this;
        }

        public Builder accountId(Long accountId) {
            this.accountId = accountId;
            return this;
        }

        public NewLetterRequest build() {
            return new NewLetterRequest(songStory, songRequest, accountId);
        }
    }
}
