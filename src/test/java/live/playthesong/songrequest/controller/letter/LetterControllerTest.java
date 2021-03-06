package live.playthesong.songrequest.controller.letter;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.playthesong.songrequest.controller.LetterController;
import live.playthesong.songrequest.controller.MockMvcResponse;
import live.playthesong.songrequest.controller.restdocs.Parameters;
import live.playthesong.songrequest.controller.restdocs.ResponseFields;
import live.playthesong.songrequest.domain.letter.Letter;
import live.playthesong.songrequest.domain.letter.request.DateParameters;
import live.playthesong.songrequest.domain.letter.request.LetterRequest;
import live.playthesong.songrequest.domain.letter.request.PaginationParameters;
import live.playthesong.songrequest.domain.letter.request.StatusChangeRequest;
import live.playthesong.songrequest.domain.letter.request.inner.SongRequest;
import live.playthesong.songrequest.domain.letter.request.inner.SongRequestBuilder;
import live.playthesong.songrequest.domain.letter.response.LettersResponse;
import live.playthesong.songrequest.domain.letter.response.inner.LetterDetails;
import live.playthesong.songrequest.global.error.exception.business.AccountMismatchException;
import live.playthesong.songrequest.global.error.exception.business.LetterNotReadyException;
import live.playthesong.songrequest.global.error.exception.business.LetterStatusException;
import live.playthesong.songrequest.global.error.response.ErrorCode;
import live.playthesong.songrequest.security.SecurityConfig;
import live.playthesong.songrequest.security.oauth.OAuthAccount;
import live.playthesong.songrequest.service.LetterService;
import live.playthesong.songrequest.testconfig.BaseControllerTest;
import live.playthesong.songrequest.testconfig.security.mockuser.WithAdmin;
import live.playthesong.songrequest.testconfig.security.mockuser.WithGuest;
import live.playthesong.songrequest.testconfig.security.mockuser.WithMember;
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

import static live.playthesong.songrequest.controller.MockMvcRequest.*;
import static live.playthesong.songrequest.domain.account.Role.*;
import static live.playthesong.songrequest.domain.letter.RequestStatus.*;
import static live.playthesong.songrequest.testobject.AccountFactory.*;
import static live.playthesong.songrequest.testobject.LetterFactory.*;
import static live.playthesong.songrequest.testobject.PaginationFactory.createPaginationParameters;
import static live.playthesong.songrequest.testobject.PaginationFactory.createPaginationParametersOf;
import static live.playthesong.songrequest.testobject.SongFactory.*;
import static java.lang.Boolean.TRUE;
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
                                                .withParam("direction", String.valueOf(parameters.getDirection()))
                                                .doRequest());

        // then
        MockMvcResponse.OK(results);
    }

    @ParameterizedTest
    @MethodSource("paginationFindAllLettersParameters")
    @DisplayName("OK - 전체 Letter 목록 페이징 경계 값 테스트")
    void pagination_find_all_letters(Integer page, Integer size, String direction) throws Exception {
        // given
        PaginationParameters parameters = createPaginationParametersOf(page, size, direction);
        LettersResponse letters = createLettersResponse();

        // when
        when(letterService.findAllLetters(refEq(parameters))).thenReturn(letters);

        ResultActions results = mockMvc.perform(get("/api/letters")
                                                .withParam("page", String.valueOf(parameters.getPage()))
                                                .withParam("size", String.valueOf(parameters.getSize()))
                                                .withParam("direction", String.valueOf(parameters.getDirection()))
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

    @Test
    @WithMember
    @DisplayName("BAD_REQUEST - 신청곡 요청이 중단된 상태에서 사용자가 Letter 등록을 요청하는 테스트")
    void bad_request_not_ready_to_create_letter() throws Exception {
        // given
        ErrorCode letterNotReadyError = ErrorCode.LETTER_NOT_READY;
        OAuthAccount memberAccount = createOAuthAccountOf(MEMBER);
        LetterRequest letterRequest = createLetterRequestOf("songStory", createSongRequest());

        // when
        when(letterService.createLetter(any(OAuthAccount.class), any(LetterRequest.class)))
                .thenThrow(new LetterNotReadyException());

        ResultActions results = mockMvc.perform(post("/api/letters")
                                                .withPrincipal(memberAccount)
                                                .withBody(objectMapper.writeValueAsString(letterRequest))
                                                .doRequest());

        // then
        MockMvcResponse.BAD_REQUEST(results, letterNotReadyError);
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
        int dayAgo = 1;

        PaginationParameters paginationParams = createPaginationParameters();
        DateParameters dateParams = new DateParameters();
        dateParams.setDayAgo(dayAgo);

        Page<Letter> lettersPage = createLettersPage();
        LettersResponse lettersResponse = LettersResponse.from(lettersPage, TRUE);

        // when
        when(letterService.findLettersByStatus(eq(DONE), refEq(paginationParams), refEq(dateParams))).thenReturn(lettersResponse);

        ResultActions results = mockMvc.perform(get("/api/letters/status/{requestStatus}", "done")
                                                .withParam("page", String.valueOf(paginationParams.getPage()))
                                                .withParam("size", String.valueOf(paginationParams.getSize()))
                                                .withParam("direction", String.valueOf(paginationParams.getDirection()))
                                                .withParam("dayAgo", String.valueOf(dateParams.getDayAgo()))
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

    @Test
    @WithAdmin
    @DisplayName("NO_CONTENT - 작성자와 일치하지 않지만 권한이 Admin인 사용자가 Letter 삭제를 요청하는 테스트")
    void admin_delete_letter() throws Exception {
        // given
        Long adminId = 1L;
        Long letterAccountId = 2L;
        OAuthAccount adminAccount = createOAuthAccountOf(adminId, ADMIN);

        Letter letter = createLetterOf(createMemberOf(letterAccountId), createSong());

        // when
        ResultActions results = mockMvc.perform(delete("/api/letters/{id}", letter.getId())
                                                .withPrincipal(adminAccount)
                                                .doRequest());

        // then
        MockMvcResponse.NO_CONTENT(results);
    }

    @Test
    @WithGuest
    @DisplayName("FORBIDDEN - 권한이 없는 (변경 된) 사용자가 Letter 삭제 API 요청 테스트")
    void forbidden_delete_letter() throws Exception {
        // given
        ErrorCode accessDeniedError = ErrorCode.ACCESS_DENIED_ERROR;
        Long accountId = 1L;
        OAuthAccount guestAccount = createOAuthAccountOf(accountId, GUEST);

        Letter letter = createLetterOf(createMemberOf(guestAccount.getId()), createSong());

        // when
        ResultActions results = mockMvc.perform(delete("/api/letters/{id}", letter.getId())
                                                .withPrincipal(guestAccount)
                                                .doRequest());

        // then
        MockMvcResponse.FORBIDDEN(results, accessDeniedError);
    }

    @Test
    @WithAdmin
    @DisplayName("OK - Letter Status 수정 API 테스트")
    void change_status() throws Exception {
        // given
        OAuthAccount adminAccount = createOAuthAccountOf(ADMIN);
        Long letterId = 1L;
        Letter doneLetter = createLetterOf(letterId, DONE, createMember(), createSong());

        StatusChangeRequest request = createStatusChangeRequestOf(DONE);
        LetterDetails updatedLetter = LetterDetails.from(doneLetter);
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        when(letterService.changeStatus(eq(doneLetter.getId()), refEq(request))).thenReturn(updatedLetter);
        ResultActions results = mockMvc.perform(put("/api/letters/{id}/status", letterId)
                                                .withPrincipal(adminAccount)
                                                .withBody(requestBody)
                                                .doRequest());

        // then
        MockMvcResponse.OK(results);
    }

    @Test
    @WithAdmin
    @DisplayName("BAD_REQUEST - 유효하지 않은 값으로 Letter Status 수정을 요청 했을때 예외가 발생하는 테스트")
    void bad_request_change_status() throws Exception {
        // given
        ErrorCode invalidLetterStatus = ErrorCode.INVALID_LETTER_STATUS;
        OAuthAccount adminAccount = createOAuthAccountOf(ADMIN);
        Long letterId = 1L;
        StatusChangeRequest request = createStatusChangeRequestOf(null);
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        when(letterService.changeStatus(eq(letterId), any())).thenThrow(new LetterStatusException());
        ResultActions results = mockMvc.perform(put("/api/letters/{id}/status", letterId)
                                                .withPrincipal(adminAccount)
                                                .withBody(requestBody)
                                                .doRequest());

        // then
        MockMvcResponse.BAD_REQUEST(results, invalidLetterStatus);
    }

    @Test
    @WithMember
    @DisplayName("FORBIDDEN - ADMIN 권한을 가지지 않은 사용자가 Status 변경 요청 했을 경우 요청이 실패하는 테스트")
    void forbidden_change_status() throws Exception {
        // given
        ErrorCode accessDeniedError = ErrorCode.ACCESS_DENIED_ERROR;
        OAuthAccount memberAccount = createOAuthAccountOf(MEMBER);
        Long letterId = 1L;
        Letter letter = createLetterOf(1L, WAITING, createMember(), createSong());

        // when
        ResultActions results = mockMvc.perform(put("/api/letters/{id}/status", letterId)
                                                .withPrincipal(memberAccount)
                                                .doRequest());

        // then
        MockMvcResponse.FORBIDDEN(results, accessDeniedError);
    }

    @Test
    @WithAdmin
    @DisplayName("OK - 오늘 등록 된 Letters 데이터를 초기화하는 테스트")
    void initialize_letters() throws Exception {
        // given
        OAuthAccount adminAccount = createOAuthAccountOf(ADMIN);

        // when
        ResultActions results = mockMvc.perform(delete("/api/letters/yesterday")
                                                .withPrincipal(adminAccount)
                                                .doRequest());

        // then
        MockMvcResponse.NO_CONTENT(results);
    }

    @Test
    @WithMember
    @DisplayName("FORBIDDEN - ADMIN 권한을 가지지 않은 사용자가 Letters 데이터 초기화를 요청했을 경우 예외가 발생하는 테스트")
    void forbidden_initialize_letters() throws Exception {
        // given
        ErrorCode accessDeniedError = ErrorCode.ACCESS_DENIED_ERROR;
        OAuthAccount memberAccount = createOAuthAccountOf(MEMBER);

        // when
        ResultActions results = mockMvc.perform(delete("/api/letters/yesterday")
                                                .withPrincipal(memberAccount)
                                                .doRequest());

        // then
        MockMvcResponse.FORBIDDEN(results, accessDeniedError);
    }

    private static Stream<Arguments> paginationFindAllLettersParameters() {
        int pageMin = 0;
        int pageSizeMin = 10;
        int pageSizeMax = 50;
        String wrongDirection = "Wrong Direction";
        return Stream.of(
                Arguments.of(null, null, wrongDirection),
                Arguments.of(null, pageSizeMax + 1, wrongDirection),
                Arguments.of(pageMin - 1, null, wrongDirection),
                Arguments.of(pageMin - 1, pageSizeMin - 1, wrongDirection),
                Arguments.of(pageMin - 1, pageSizeMax + 1, wrongDirection)
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
                Arguments.of("Title", "Artist", StringUtils.repeat("http://Invalid_imageUrl", 15),
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
