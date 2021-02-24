package com.requestrealpiano.songrequest.domain.letter.request.inner;

public class SongRequestBuilder {

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String title;
        private String artist;
        private String imageUrl;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder artist(String artist) {
            this.artist = artist;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public SongRequest build() {
            return new SongRequest(title, artist, imageUrl);
        }
    }
}
