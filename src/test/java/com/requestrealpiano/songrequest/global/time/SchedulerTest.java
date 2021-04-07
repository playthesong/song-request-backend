package com.requestrealpiano.songrequest.global.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class SchedulerTest {

    @InjectMocks
    Scheduler scheduler;

    @ParameterizedTest
    @MethodSource("defaultStartDateTimeParameters")
    @DisplayName("기본 조회 시작 시간 테스트")
    void default_start_date_time(int endYear, int endMonth, int endDayOfMonth, int endHour, int endMinute, int endSecond,
                                 int startYear, int startMonth, int startDayOfMonth, int defaultHour, int defaultMinute, int defaultSecond) {
        // given

        LocalDateTime endDateTime = LocalDateTime.of(endYear, endMonth, endDayOfMonth, endHour, endMinute, endSecond);

        // when
        LocalDateTime startDateTime = scheduler.defaultStartDateTimeFrom(endDateTime);

        // then
        assertAll(
                () -> assertThat(startDateTime.getYear()).isEqualTo(startYear),
                () -> assertThat(startDateTime.getMonthValue()).isEqualTo(startMonth),
                () -> assertThat(startDateTime.getDayOfMonth()).isEqualTo(startDayOfMonth),
                () -> assertThat(startDateTime.getHour()).isEqualTo(defaultHour),
                () -> assertThat(startDateTime.getMinute()).isEqualTo(defaultMinute),
                () -> assertThat(startDateTime.getSecond()).isEqualTo(defaultSecond)

        );
    }

    @ParameterizedTest
    @MethodSource("initializationStartDateTimeParameters")
    @DisplayName("신청곡 초기화 시작 시간 설정 테스트")
    void initialization_start_date_time(int endYear, int endMonth, int endDayOfMonth, int endHour, int endMinute, int endSecond,
                                        int startYear, int startMonth, int startDayOfMonth,
                                        int initializationHour, int initializationMinute, int initializationSecond) {
        // given
        LocalDateTime endDateTime = LocalDateTime.of(endYear, endMonth, endDayOfMonth, endHour, endMinute, endSecond);

        // when
        LocalDateTime startDateTime = scheduler.initializationStartDateTimeFrom(endDateTime);

        // then
        assertAll(
                () -> assertThat(startDateTime.getYear()).isEqualTo(startYear),
                () -> assertThat(startDateTime.getMonthValue()).isEqualTo(startMonth),
                () -> assertThat(startDateTime.getDayOfMonth()).isEqualTo(startDayOfMonth),
                () -> assertThat(startDateTime.getHour()).isEqualTo(initializationHour),
                () -> assertThat(startDateTime.getMinute()).isEqualTo(initializationMinute),
                () -> assertThat(startDateTime.getSecond()).isEqualTo(initializationSecond)

        );
    }

    private static Stream<Arguments> initializationStartDateTimeParameters() {
        return Stream.of(
                Arguments.of(2021, 4, 1, 14, 10, 11,
                             2021, 3, 31, 18, 59, 0)
        );
    }

    private static Stream<Arguments> defaultStartDateTimeParameters() {
        return Stream.of(
                Arguments.of(2021, 4, 1, 14, 10, 11,
                             2021, 3, 31, 19, 0, 0)
        );
    }
}
