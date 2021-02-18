package com.requestrealpiano.songrequest.domain.letter.dto.request.inner;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SongRequest {

    @NotEmpty(message = "제목은 필수 입니다.")
    @Size(min = 1, max = 30, message = "제목은 30자 미만 입니다.")
    private String title;

    @NotEmpty(message = "아티스트 정보는 필수 입니다.")
    @Size(min = 1, max = 30, message = "아티스트는 30자 미만 입니다.")
    private String artist;

    @Size(max = 100, message = "유효한 이미지 정보가 아닙니다.")
    private String imageUrl;

    SongRequest(String title, String artist, String imageUrl) {
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
    }
}
