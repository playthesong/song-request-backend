package com.requestrealpiano.songrequest.global.time;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class Scheduler {

    private final int DEFAULT_HOUR = 12;
    private final int DEFAULT_MINUTE = 0;
    private final int DEFAULT_SECOND = 0;

    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    public LocalDateTime defaultStartDateTimeFrom(LocalDateTime today) {
        LocalDate startDate = today.minusDays(1).toLocalDate();
        LocalTime startTime = LocalTime.of(DEFAULT_HOUR, DEFAULT_MINUTE, DEFAULT_SECOND);
        return LocalDateTime.of(startDate, startTime);
    }
}
