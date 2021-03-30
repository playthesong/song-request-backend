package com.requestrealpiano.songrequest.controller.letter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.requestrealpiano.songrequest.controller.LetterController;
import com.requestrealpiano.songrequest.controller.MockMvcResponse;
import com.requestrealpiano.songrequest.controller.restdocs.Parameters;
import com.requestrealpiano.songrequest.controller.restdocs.ResponseFields;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.request.LetterRequest;
import com.requestrealpiano.songrequest.domain.letter.request.PaginationParameters;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequestBuilder;
import com.requestrealpiano.songrequest.domain.letter.response.LettersResponse;
import com.requestrealpiano.songrequest.domain.letter.response.inner.LetterDetails;
import com.requestrealpiano.songrequest.global.error.exception.business.AccountMismatchException;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import com.requestrealpiano.songrequest.security.SecurityConfig;
import com.requestrealpiano.songrequest.security.oauth.OAuthAccount;
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
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static com.requestrealpiano.songrequest.controller.MockMvcRequest.*;
import static com.requestrealpiano.songrequest.domain.account.Role.GUEST;
import static com.requestrealpiano.songrequest.domain.account.Role.MEMBER;
import static com.requestrealpiano.songrequest.domain.letter.RequestStatus.DONE;
import static com.requestrealpiano.songrequest.testobject.AccountFactory.*;
import static com.requestrealpiano.songrequest.testobject.LetterFactory.*;
import static com.requestrealpiano.songrequest.testobject.PaginationFactory.createPaginationParameters;
import static com.requestrealpiano.songrequest.testobject.PaginationFactory.createPaginationParametersOf;
import static com.requestrealpiano.songrequest.testobject.SongFactory.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
        PaginationParameters parameters = createPaginationParameters();
        LettersResponse letters = createLettersResponse();

        // when
        when(letterService.findAllLetters(refEq(parameters))).thenReturn(letters);

        ResultActions results = mockMvc.perform(get("/api/letters")
                                                .withParam("page", String.valueOf(parameters.getPage()))
                                                .withParam("size", String.valueOf(parameters.getSize()))
                                                .doRequest());

        // then
        MockMvcResponse.OK(results);
    }

    @ParameterizedTest
    @MethodSource("paginationFindAllLettersParameters")
    @DisplayName("OK - 전체 Letter 목록 페이징 경계 값 테스트")
    void pagination_find_all_letters(Integer page, Integer size) throws Exception {
        // given
        PaginationParameters parameters = createPaginationParametersOf(page, size);
        LettersResponse letters = createLettersResponse();

        // when
        when(letterService.findAllLetters(refEq(parameters))).thenReturn(letters);

        ResultActions results = mockMvc.perform(get("/api/letters")
                                                .withParam("page", String.valueOf(parameters.getPage()))
                                                .withParam("size", String.valueOf(parameters.getSize()))
                                                .doRequest());

        // then
        MockMvcResponse.OK(results);
    }

    @Test
    @DisplayName("OK - 유효한 ID 로부터 Letter 상세정보를 조회하는 API 테스트")
    void find_letter_by_valid_id() throws Exception {
        // given
        LetterDetails letterDetails = createLetterDetails();
        Long letterId = letterDetails.getId();

        // when
        when(letterService.findLetter(letterId)).thenReturn(letterDetails);

        ResultActions results = mockMvc.perform(get("/api/letters/{id}", letterId)
                                                .doRequest());

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
        OAuthAccount loginAccount = createOAuthAccountOf(accountId, MEMBER);
        LetterRequest letterRequest = createLetterRequestOf(songStory, songRequest);
        LetterDetails response = createLetterDetails();
        String requestBody = objectMapper.writeValueAsString(letterRequest);

        // when
        when(letterService.createLetter(any(OAuthAccount.class), any(LetterRequest.class))).thenReturn(response);
        ResultActions results = mockMvc.perform(post("/api/letters")
                                                .withPrincipal(loginAccount)
                                                .withBody(requestBody)
                                                .doRequest());

        // then
        MockMvcResponse.CREATED(results);
    }

    @ParameterizedTest
    @WithMember
    @MethodSource("invalidNewLetterRequestParameters")
    @DisplayName("BAD_REQUEST - 유효하지 않은 값으로 Letter 등록 요청 테스트")
    void invalid_new_letter_request(String title, String artist, String imageUrl, String songStory,
                                    Long accountId) throws Exception {
        // given
        OAuthAccount loginAccount = createOAuthAccountOf(accountId, MEMBER);
        SongRequest songRequest = createSongRequestOf(title, artist, imageUrl);
        LetterRequest letterRequest = createLetterRequestOf(songStory, songRequest);
        String requestBody = objectMapper.writeValueAsString(letterRequest);
        ErrorCode invalidInputError = ErrorCode.INVALID_INPUT_VALUE;

        // when
        ResultActions results = mockMvc.perform(post("/api/letters")
                                                .withPrincipal(loginAccount)
                                                .withBody(requestBody)
                                                .doRequest());

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
        OAuthAccount loginAccount = createOAuthAccountOf(accountId, GUEST);
        SongRequest songRequest = createSongRequestOf(title, artist, imageUrl);
        LetterRequest letterRequest = createLetterRequestOf(songStory, songRequest);
        ErrorCode accessDeniedError = ErrorCode.ACCESS_DENIED_ERROR;
        String requestBody = objectMapper.writeValueAsString(letterRequest);

        // when
        ResultActions results = mockMvc.perform(post("/api/letters")
                                                .withPrincipal(loginAccount)
                                                .withBody(requestBody)
                                                .doRequest());

        // then
        MockMvcResponse.FORBIDDEN(results, accessDeniedError)
                       .andDo(document("create-letter-unauthorized",
                               responseFields(ResponseFields.error())
                       ))
        ;
    }

    @Test
    @WithMember
    @DisplayName("OK - 유효한 Letter Status 값으로 요청하는 테스트")
    void valid_letter_status() throws Exception {
        // given
        PaginationParameters parameters = createPaginationParameters();
        Page<Letter> lettersPage = createLettersPage();
        LettersResponse lettersResponse = LettersResponse.from(lettersPage);

        // when
        when(letterService.findLettersByStatus(eq(DONE), refEq(parameters))).thenReturn(lettersResponse);

        ResultActions results = mockMvc.perform(get("/api/letters/status/{requestStatus}", "done")
                                                .withParam("page", String.valueOf(parameters.getPage()))
                                                .withParam("size", String.valueOf(parameters.getSize()))
                                                .doRequest());

        // then
        MockMvcResponse.OK(results);
    }

    @ParameterizedTest
    @MethodSource("invalidLetterStatusParameters")
    @WithGuest
    @DisplayName("BAD_REQUEST - 유효하지 않은 Letter Status로 요청 했을 경우 예외가 발생하는 테스트")
    void invalid_letter_status(String wrongStatus) throws Exception {
        // given
        ErrorCode invalidRequestError = ErrorCode.INVALID_REQUEST_ERROR;

        // when
        ResultActions results = mockMvc.perform(get("/api/letters/status/{requestStatus}", wrongStatus)
                                                .doRequest());

        // then
        MockMvcResponse.BAD_REQUEST(results, invalidRequestError);
    }

    @Test
    @WithMember
    @DisplayName("OK - Letter 수정 API 테스트")
    void update_letter() throws Exception {
        // given
        Long accountId = 1L;
        OAuthAccount loginAccount = createOAuthAccountOf(accountId, MEMBER);

        LetterRequest letterRequest = createLetterRequestOf("NewSongStory", createSongRequest());
        String requestBody = objectMapper.writeValueAsString(letterRequest);

        Letter letter = createLetterOf(createMemberOf(loginAccount.getId()), createSong());
        LetterDetails letterDetails = LetterDetails.from(letter);

        // when
        when(letterService.updateLetter(any(OAuthAccount.class), eq(letter.getId()), any(LetterRequest.class)))
                .thenReturn(letterDetails);

        ResultActions results = mockMvc.perform(put("/api/letters/{id}", letter.getId())
                                                .withPrincipal(loginAccount)
                                                .withBody(requestBody)
                                                .doRequest());

        // then
        MockMvcResponse.OK(results);
    }

    @Test
    @WithMember
    @DisplayName("BAD_REQUEST - 일치하지 않는 사용자가 Letter 수정을 요청하는 테스트")
    void bad_request_update_letter() throws Exception {
        // given
        Long loginId = 1L;
        Long letterAccountId = 2L;
        OAuthAccount loginAccount = createOAuthAccountOf(loginId, MEMBER);

        LetterRequest letterRequest = createLetterRequestOf("NewSongStory", createSongRequest());
        String requestBody = objectMapper.writeValueAsString(letterRequest);

        Letter letter = createLetterOf(createMemberOf(letterAccountId), createSong());
        ErrorCode accountMismatchError = ErrorCode.ACCOUNT_MISMATCH_ERROR;

        // when
        when(letterService.updateLetter(any(OAuthAccount.class), eq(letter.getId()), any(LetterRequest.class)))
                .thenThrow(new AccountMismatchException());

        ResultActions results = mockMvc.perform(put("/api/letters/{id}", letter.getId())
                                                .withPrincipal(loginAccount)
                                                .withBody(requestBody)
                                                .doRequest());

        // then
        MockMvcResponse.BAD_REQUEST(results, accountMismatchError);
    }

    @Test
    @WithGuest
    @DisplayName("FORBIDDEN - 권한이 없는 (변경 된) 사용자가 Letter 수정을 요청하는 테스트")
    void forbidden_update_letter() throws Exception {
        // given
        Long loginId = 1L;
        OAuthAccount guestAccount = createOAuthAccountOf(loginId, GUEST);

        LetterRequest letterRequest = createLetterRequestOf("NewSongStory", createSongRequest());
        String requestBody = objectMapper.writeValueAsString(letterRequest);

        Letter letter = createLetterOf(createGuestOf(guestAccount.getId()), createSong());
        ErrorCode accessDeniedError = ErrorCode.ACCESS_DENIED_ERROR;

        // when
        ResultActions results = mockMvc.perform(put("/api/letters/{id}", letter.getId())
                                                .withPrincipal(guestAccount)
                                                .withBody(requestBody)
                                                .doRequest());

        // then
        MockMvcResponse.FORBIDDEN(results, accessDeniedError);
    }

    @Test
    @WithMember
    @DisplayName("NO_CONTENT - Letter 삭제 API 테스트")
    void delete_letter() throws Exception {
        // given
        Long accountId = 1L;
        OAuthAccount loginAccount = createOAuthAccountOf(accountId, MEMBER);

        Letter letter = createLetterOf(createMemberOf(loginAccount.getId()), createSong());

        // when
        ResultActions results = mockMvc.perform(delete("/api/letters/{id}", letter.getId())
                                                .withPrincipal(loginAccount)
                                                .doRequest());

        // then
        MockMvcResponse.NO_CONTENT(results);
    }

    private static Stream<Arguments> paginationFindAllLettersParameters() {
        int pageMin = 0;
        int pageSizeMin = 10;
        int pageSizeMax = 50;
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(null, pageSizeMax + 1),
                Arguments.of(pageMin - 1, null),
                Arguments.of(pageMin - 1, pageSizeMin - 1),
                Arguments.of(pageMin - 1, pageSizeMax + 1)
        );
    }

    private static Stream<Arguments> invalidLetterStatusParameters() {
        return Stream.of(
                Arguments.of("Something Wrong Variable"),
                Arguments.of("12345"),
                Arguments.of("Number Mixed - 12345")
        );
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
