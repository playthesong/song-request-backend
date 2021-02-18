package com.requestrealpiano.songrequest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.requestrealpiano.songrequest.domain.letter.dto.request.NewLetterRequest;
import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequestBuilder;
import com.requestrealpiano.songrequest.domain.letter.dto.response.LetterResponse;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import com.requestrealpiano.songrequest.service.LetterService;
import com.requestrealpiano.songrequest.testconfig.RestDocsConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Stream;

import static com.requestrealpiano.songrequest.testobject.LetterFactory.*;
import static com.requestrealpiano.songrequest.testobject.SongFactory.createSongRequestOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = RestDocsConfiguration.class)
@AutoConfigureRestDocs
@WebMvcTest(controllers = LetterController.class)
class LetterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LetterService letterService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("OK - 전체 신청곡 목록 조회 API 테스트")
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
                     .andDo(document("find-letters",
                         responseFields(
                             fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("API 요청 성공 여부"),
                             fieldWithPath("statusMessage").type(JsonFieldType.STRING).description("상태 메시지"),
                             fieldWithPath("data").type(JsonFieldType.ARRAY).description("신청곡 목록 데이터"),
                             fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("신청곡 ID"),
                             fieldWithPath("data[].songStory").type(JsonFieldType.STRING).description("사연 내용"),
                             fieldWithPath("data[].requestStatus").type(JsonFieldType.STRING).description("신청곡 요청 상태"),
                             fieldWithPath("data[].createdDateTime").type(JsonFieldType.STRING).description("신청곡 등록 일시"),
                             fieldWithPath("data[].song").type(JsonFieldType.OBJECT).description("신청곡 음원 정보"),
                             fieldWithPath("data[].song.id").type(JsonFieldType.NUMBER).description("신청곡 음원 ID"),
                             fieldWithPath("data[].song.title").type(JsonFieldType.STRING).description("음원 제목"),
                             fieldWithPath("data[].song.artist").type(JsonFieldType.STRING).description("아티스트"),
                             fieldWithPath("data[].song.imageUrl").type(JsonFieldType.STRING).description("앨범 이미지"),
                             fieldWithPath("data[].account").type(JsonFieldType.OBJECT).description("신청자 정보"),
                             fieldWithPath("data[].account.id").type(JsonFieldType.NUMBER).description("신청자 ID"),
                             fieldWithPath("data[].account.name").type(JsonFieldType.STRING).description("신청자 이름"),
                             fieldWithPath("data[].account.avatarUrl").type(JsonFieldType.STRING).description("신청자 프로필 이미지")
                         )
                     ))
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
    @DisplayName("OK - 새로운 Letter 생성 API 테스트")
    void create_new_Letter(String songStory, SongRequest songRequest, Long accountId) throws Exception {
        // given
        NewLetterRequest newLetterRequest = createNewLetterRequestOf(songStory, songRequest, accountId);
        LetterResponse response = createLetterResponse();

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

    @ParameterizedTest
    @MethodSource("invalidNewLetterRequestParameters")
    @DisplayName("BAD_REQUEST - 유효하지 않은 값으로 Letter 등록 요청 테스트")
    void invalid_new_letter_request(String title, String artist, String imageUrl, String songStory,
                                    Long accountId) throws Exception {
        // given
        SongRequest songRequest = createSongRequestOf(title, artist, imageUrl);
        NewLetterRequest newLetterRequest = createNewLetterRequestOf(songStory, songRequest, accountId);

        // when
        ResultActions resultActions = mockMvc.perform(post("/letters")
                                                      .accept(MediaType.APPLICATION_JSON)
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(objectMapper.writeValueAsString(newLetterRequest)));

        // then
        resultActions.andDo(print())
                     .andExpect(status().isBadRequest())
                     .andExpect(jsonPath("statusCode").value(ErrorCode.INVALID_INPUT_VALUE.getStatusCode()))
                     .andExpect(jsonPath("message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()))
                     .andExpect(jsonPath("errors").isNotEmpty())
        ;
    }

    private static Stream<Arguments> invalidNewLetterRequestParameters() {
        return Stream.of(
                Arguments.of("", "Artist", "http://imageUrl", "Song story", 1L),
                Arguments.of("Title", "", "http://imageUrl", "Song story", 1L),
                Arguments.of(StringUtils.repeat("Invalid length of title", 100), "Artist", "http://imageUrl",
                        "Song story", 1L),
                Arguments.of("Title", StringUtils.repeat("Invalid length of Artist", 5), "http://imageUrl",
                        "Song story", 1L),
                Arguments.of("Title", "Artist", StringUtils.repeat("http://Invalid_imageUrl", 10),
                        "Song story", 1L),
                Arguments.of("Title", "Artist", "http://imageUrl",
                        StringUtils.repeat("Song Story", 100), 1L)
        );
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
