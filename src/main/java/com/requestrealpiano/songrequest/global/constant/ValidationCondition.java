package com.requestrealpiano.songrequest.global.constant;

public class ValidationCondition {

    // Common
    public static final String NOT_EMPTY_MESSAGE = "필수 입력 정보 입니다.";

    // Paging
    public static final String PAGE_MESSAGE = "최소 1페이지부터 요청이 가능 합니다.";
    public static final int PAGE_MIN = 0;

    public static final String PAGE_SIZE_MESSAGE = "허용 요청 개수는 최소 10개 최대 50개 입니다.";
    public static final int PAGE_SIZE_DEFAULT = 20;
    public static final int PAGE_SIZE_MIN = 10;
    public static final int PAGE_SIZE_MAX = 50;

    public static final int DAY_AGO_MIN = 0;
    public static final int DAY_AGO_DEFAULT = 1;

    // Artist
    public static final String ARTIST_MESSAGE = "아티스트는 30자 미만 입니다.";
    public static final int ARTIST_MAX = 30;
    public static final int ARTIST_MIN = 1;

    // Title
    public static final String TITLE_MESSAGE = "제목은 50자 미만 입니다.";
    public static final int TITLE_MAX = 50;
    public static final int TITLE_MIN = 1;

    // Image URL
    public static final String IMAGE_MESSAGE = "유효한 이미지 정보가 아닙니다.";
    public static final int IMAGE_MAX = 300;

    // Song Story
    public static final String SONG_STORY_MESSAGE = "사연은 500자 미만이어야 합니다.";
    public static final int SONG_STORY_MAX = 500;
}
