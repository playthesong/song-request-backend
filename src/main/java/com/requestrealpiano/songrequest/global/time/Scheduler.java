package com.requestrealpiano.songrequest.global.time;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class Scheduler {

    private final int DEFAULT_HOUR = 19;
    private final int DEFAULT_MINUTE = 0;
    private final int DEFAULT_SECOND = 0;

    private final int INITIALIZATION_HOUR = 18;
    private final int INITIALIZATION_MINUTE = 59;

    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    public LocalDateTime defaultStartDateTimeFrom(LocalDateTime today) {
        // 신청곡 조회 기준 시간은 하루 전 19시부터 현재시간까지 입니다.
        LocalDate startDate = today.minusDays(1).toLocalDate();
        LocalTime startTime = LocalTime.of(DEFAULT_HOUR, DEFAULT_MINUTE, DEFAULT_SECOND);
        return LocalDateTime.of(startDate, startTime);
    }

    public LocalDateTime initializationStartDateTimeFrom(LocalDateTime today) {
        // 라이브 시작 전 신청곡 목록 초기화 기준 시간은 하루 전 18시 59분부터 현재시간까지 입니다.
        LocalDate startDate = today.minusDays(1).toLocalDate();
        LocalTime startTime = LocalTime.of(INITIALIZATION_HOUR, INITIALIZATION_MINUTE, DEFAULT_SECOND);
        return LocalDateTime.of(startDate, startTime);
    }

    public LocalDateTime customStartDateTimeFrom(LocalDateTime today, int dayAgo) {
        LocalDate startDate = today.minusDays(dayAgo).toLocalDate();
        LocalTime startTime = LocalTime.of(DEFAULT_HOUR, DEFAULT_MINUTE, DEFAULT_SECOND);
        return LocalDateTime.of(startDate, startTime);
    }
}
