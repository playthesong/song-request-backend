package com.requestrealpiano.songrequest.domain.letter.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DateParametersTest {

    @Test
    @DisplayName("Day Ago 경계 값 테스트")
    void boundary_value_set_day_ago() {
        // given
        DateParameters parameters = new DateParameters();
        int minimumDayAgo = 0;
        int defaultDayAgo = 1;

        // when
        parameters.setDayAgo(minimumDayAgo);

        // then
        assertThat(parameters.getDayAgo()).isEqualTo(defaultDayAgo);
    }
}
