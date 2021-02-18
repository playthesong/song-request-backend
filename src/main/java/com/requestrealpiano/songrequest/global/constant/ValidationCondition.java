package com.requestrealpiano.songrequest.global.constant;

public interface ValidationCondition {

    // Common
    String NOT_EMPTY_MESSAGE = "필수 입력 정보 입니다.";

    // Artist
    String ARTIST_MESSAGE = "아티스트는 30자 미만 입니다.";
    int ARTIST_MAX = 30;
    int ARTIST_MIN = 1;

    // Title
    String TITLE_MESSAGE = "제목은 30자 미만 입니다.";
    int TITLE_MAX = 30;
    int TITLE_MIN = 1;

    // Image URL
    String IMAGE_MESSAGE = "유효한 이미지 정보가 아닙니다.";
    int IMAGE_MAX = 100;

    // Song Story
    String SONG_STORY_MESSAGE = "사연은 500자 미만이어야 합니다.";
    int SONG_STORY_MAX = 500;
}
