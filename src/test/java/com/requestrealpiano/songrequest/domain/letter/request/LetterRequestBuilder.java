package com.requestrealpiano.songrequest.domain.letter.request;

import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;

public class LetterRequestBuilder {

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String songStory;
        private SongRequest songRequest;

        public Builder songStory(String songStory) {
            this.songStory = songStory;
            return this;
        }

        public Builder songRequest(SongRequest song) {
            this.songRequest = song;
            return this;
        }

        public LetterRequest build() {
            return new LetterRequest(songStory, songRequest);
        }
    }
}
