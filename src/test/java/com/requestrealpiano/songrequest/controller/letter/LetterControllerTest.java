package com.requestrealpiano.songrequest.controller.letter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.requestrealpiano.songrequest.controller.LetterController;
import com.requestrealpiano.songrequest.controller.MockMvcRequest;
import com.requestrealpiano.songrequest.controller.MockMvcResponse;
import com.requestrealpiano.songrequest.controller.restdocs.Parameters;
import com.requestrealpiano.songrequest.controller.restdocs.RequestFields;
import com.requestrealpiano.songrequest.controller.restdocs.ResponseFields;
import com.requestrealpiano.songrequest.domain.letter.request.NewLetterRequest;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequestBuilder;
import com.requestrealpiano.songrequest.domain.letter.response.LetterResponse;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import com.requestrealpiano.songrequest.security.SecurityConfig;
import com.requestrealpiano.songrequest.service.LetterService;
import com.requestrealpiano.songrequest.testconfig.BaseControllerTest;
import com.requestrealpiano.songrequest.testconfig.security.mockuser.WithGuest;
import com.requestrealpiano.songrequest.testconfig.security.mockuser.WithMember;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Stream;

import static com.requestrealpiano.songrequest.testobject.LetterFactory.*;
import static com.requestrealpiano.songrequest.testobject.SongFactory.createSongRequestOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@WebMvcTest(controllers = LetterController.class,
            excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
class LetterControllerTest extends BaseControllerTest {

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

        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/letters"));

        // then
        MockMvcResponse.OK(results)
                       .andDo(document("find-letters",
                           responseFields(ResponseFields.common())
                                   .andWithPrefix("data[].", ResponseFields.letter()),
                           responseFields(beneathPath("data[].song.").withSubsectionId("song"), ResponseFields.song()),
                           responseFields(beneathPath("data[].account.").withSubsectionId("account"), ResponseFields.account())
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

        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/letters/{id}", letterId));

        // then
        MockMvcResponse.OK(results)
                       .andDo(document("find-letter",
                               pathParameters(Parameters.letterId()),
                               responseFields(ResponseFields.common())
                                       .andWithPrefix("data.", ResponseFields.letter()),
                               responseFields(beneathPath("data.song.").withSubsectionId("song"), ResponseFields.song()),
                               responseFields(beneathPath("data.account.").withSubsectionId("account"), ResponseFields.account())
                       ))
        ;
    }

    @ParameterizedTest
    @WithMember
    @MethodSource("createNewLetterParameters")
    @DisplayName("OK - 새로운 Letter 등록 API 테스트")
    void create_new_Letter(String songStory, SongRequest songRequest, Long accountId) throws Exception {
        // given
        NewLetterRequest newLetterRequest = createNewLetterRequestOf(songStory, songRequest, accountId);
        LetterResponse response = createLetterResponse();
        String requestBody = objectMapper.writeValueAsString(newLetterRequest);

        // when
        when(letterService.createNewLetter(any(NewLetterRequest.class))).thenReturn(response);

        ResultActions results = mockMvc.perform(MockMvcRequest.post("/api/letters", requestBody));

        // then
        MockMvcResponse.OK(results)
                       .andDo(document("create-letter",
                               requestFields(RequestFields.createLetter()),
                               responseFields(ResponseFields.common())
                                   .andWithPrefix("data.", ResponseFields.letter())
                       ))
        ;
    }

    @ParameterizedTest
    @WithMember
    @MethodSource("invalidNewLetterRequestParameters")
    @DisplayName("BAD_REQUEST - 유효하지 않은 값으로 Letter 등록 요청 테스트")
    void invalid_new_letter_request(String title, String artist, String imageUrl, String songStory,
                                    Long accountId) throws Exception {
        // given
        SongRequest songRequest = createSongRequestOf(title, artist, imageUrl);
        NewLetterRequest newLetterRequest = createNewLetterRequestOf(songStory, songRequest, accountId);
        String requestBody = objectMapper.writeValueAsString(newLetterRequest);
        ErrorCode invalidInputError = ErrorCode.INVALID_INPUT_VALUE;

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.post("/api/letters", requestBody));

        // then
        MockMvcResponse.BAD_REQUEST(results, invalidInputError)
                       .andDo(document("create-letter-invalid-parameters",
                               responseFields(ResponseFields.error())
                       ))
        ;
    }

    @ParameterizedTest
    @MethodSource("accessDeniedUserCreateLetterParameters")
    @WithGuest
    @DisplayName("FORBIDDEN - 권한이 없는 사용자가 Letter 등록을 요청하는 테스트")
    void access_denied_user_create_letter(String title, String artist, String imageUrl, String songStory,
                                         Long accountId) throws Exception {
        // given
        SongRequest songRequest = createSongRequestOf(title, artist, imageUrl);
        NewLetterRequest newLetterRequest = createNewLetterRequestOf(songStory, songRequest, accountId);
        ErrorCode accessDeniedError = ErrorCode.ACCESS_DENIED_ERROR;
        String requestBody = objectMapper.writeValueAsString(newLetterRequest);

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.post("/api/letters", requestBody));

        // then
        MockMvcResponse.FORBIDDEN(results, accessDeniedError)
                       .andDo(document("create-letter-unauthorized",
                               responseFields(ResponseFields.error())
                       ))
        ;
    }

    private static Stream<Arguments> accessDeniedUserCreateLetterParameters() {
        return Stream.of(
            Arguments.of("New Title", "New Artist", "http://imageUrl", "Song story", 1L)
        );
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
