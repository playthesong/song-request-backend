package com.requestrealpiano.songrequest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import com.requestrealpiano.songrequest.domain.letter.dto.request.NewLetterRequest;
import com.requestrealpiano.songrequest.domain.letter.dto.request.NewLetterRequestBuilder;
import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequestBuilder;
import com.requestrealpiano.songrequest.domain.letter.dto.response.LetterResponse;
import com.requestrealpiano.songrequest.domain.letter.dto.response.inner.AccountSummary;
import com.requestrealpiano.songrequest.domain.letter.dto.response.inner.SongSummary;
import com.requestrealpiano.songrequest.service.LetterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.requestrealpiano.songrequest.testobject.LetterFactory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LetterController.class)
class LetterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LetterService letterService;

    @Test
    @DisplayName("전체 신청곡 목록 조회 API 테스트")
    void find_all_letters() throws Exception {
        // given
        List<LetterResponse> letterResponses = createLetterResponses();

        // when
        when(letterService.findAllLetters()).thenReturn(letterResponses);
        ResultActions resultActions = mockMvc.perform(get("/letters")
                                                      .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andDo(print())
                     .andExpect(status().isOk())
                     .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                     .andExpect(jsonPath("success").value(true))
                     .andExpect(jsonPath("statusMessage").value("OK"))
                     .andExpect(jsonPath("data").isArray())
                     .andExpect(jsonPath("data.length()").value(letterResponses.size()))
        ;
    }

    @Test
    @DisplayName("OK - 유효한 ID 로부터 Letter 상세정보를 조회하는 API 테스트")
    void find_letter_by_valid_id() throws Exception {
        // given
        LetterResponse letterResponse = createLetterResponse();
        Long letterId = letterResponse.getId();

        // when
        when(letterService.findLetter(letterId)).thenReturn(letterResponse);

        ResultActions resultActions = mockMvc.perform(get("/letters/{id}", letterId)
                                                      .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andDo(print())
                     .andExpect(status().isOk())
                     .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                     .andExpect(jsonPath("success").value(true))
                     .andExpect(jsonPath("statusMessage").value("OK"))
                     .andExpect(jsonPath("data").isNotEmpty())
        ;
    }

    @ParameterizedTest
    @MethodSource("createNewLetterParameters")
    @DisplayName("새로운 Letter 생성 API 테스트")
    void create_new_Letter(String songStory, SongRequest songRequest, Long accountId) throws Exception {
        // given
        NewLetterRequest newLetterRequest = createNewLetterRequestOf(songStory, songRequest, accountId);
        LetterResponse response = createLetterResponse();
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        when(letterService.createNewLetter(any(NewLetterRequest.class))).thenReturn(response);
        ResultActions resultActions = mockMvc.perform(post("/letters")
                                                      .accept(MediaType.APPLICATION_JSON)
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(objectMapper.writeValueAsString(newLetterRequest)));

        // then
        resultActions.andDo(print())
                     .andExpect(status().isOk())
                     .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                     .andExpect(jsonPath("success").value(true))
                     .andExpect(jsonPath("statusMessage").value("OK"))
                     .andExpect(jsonPath("data").isNotEmpty())
        ;
    }

    private static Stream<Arguments> createNewLetterParameters() {
        return Stream.of(
                Arguments.of("Song story",
                             SongRequestBuilder.newBuilder()
                                               .title("Song title")
                                               .artist("Artist")
                                               .imageUrl("http://imageUrl")
                                               .build(),
                             1L)
        );
    }
}
