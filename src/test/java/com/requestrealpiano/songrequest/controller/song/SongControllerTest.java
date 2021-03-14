package com.requestrealpiano.songrequest.controller.song;

import com.requestrealpiano.songrequest.controller.MockMvcRequest;
import com.requestrealpiano.songrequest.controller.MockMvcResponse;
import com.requestrealpiano.songrequest.controller.SongController;
import com.requestrealpiano.songrequest.controller.restdocs.Parameters;
import com.requestrealpiano.songrequest.controller.restdocs.ResponseFields;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.inner.LastFmTrack;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.inner.Track;
import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import com.requestrealpiano.songrequest.security.SecurityConfig;
import com.requestrealpiano.songrequest.service.SongService;
import com.requestrealpiano.songrequest.testconfig.BaseControllerTest;
import com.requestrealpiano.songrequest.testconfig.security.mockuser.WithGuest;
import com.requestrealpiano.songrequest.testconfig.security.mockuser.WithMember;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SongController.class,
            excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
class SongControllerTest extends BaseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SongService songService;

    @ParameterizedTest
    @MethodSource("searchManiaDbParameters")
    @WithMember
    @DisplayName("OK - ManiaDB 검색 결과 반환 API 테스트")
    void search_mania_db(String artist, String title) throws Exception {
        // given
        SearchApiResponse maniaDbResponse = createMockManiaDbResponse();

        // when
        when(songService.searchSong(artist, title)).thenReturn(maniaDbResponse);

        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/songs")
                                                              .param("artist", artist)
                                                              .param("title", title));

        // then
        MockMvcResponse.OK(results)
                       .andDo(document("search-song",
                               requestParameters(Parameters.searchSong()),
                               responseFields(ResponseFields.common())
                                       .andWithPrefix("data.", ResponseFields.searchSongResult())
                       ))
        ;
    }

    @ParameterizedTest
    @MethodSource("searchByInvalidParams")
    @WithMember
    @DisplayName("BAD_REQUEST - 유효하지 않은 제목, 아티스트로 요청 테스트")
    void search_by_invalid_params(String artist, String title) throws Exception {
        // given
        ErrorCode invalidInputValueError = ErrorCode.INVALID_INPUT_VALUE;

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/songs")
                                                              .param("artist", artist)
                                                              .param("title", title));


        // then
        MockMvcResponse.BAD_REQUEST(results, invalidInputValueError)
                       .andDo(document("search-song-error",
                               responseFields(ResponseFields.error())
                       ))
        ;
    }

    @ParameterizedTest
    @MethodSource("searchLastFmApiParameters")
    @WithMember
    @DisplayName("OK - LastFM 검색 결과 반환 API 테스트")
    void search_lastfm_api(String artist, String title, String imageUrl) throws Exception {
        // given
        LastFmTrack track = LastFmTrack.builder()
                                       .artist(artist)
                                       .title(title)
                                       .imageUrl(imageUrl)
                                       .build();
        List<LastFmTrack> tracks = Collections.singletonList(track);
        SearchApiResponse response = SearchApiResponse.from(tracks);

        // when
        when(songService.searchSong(artist, title)).thenReturn(response);

        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/songs")
                                                             .param("artist", artist)
                                                             .param("title", title));

        // then
        MockMvcResponse.OK(results);
    }

    @ParameterizedTest
    @MethodSource("searchManiaDbParameters")
    @WithGuest
    @DisplayName("FORBIDDEN - 권한이 없는 사용자가 신청곡 검색을 요청하는 테스트")
    void access_denied_user_search_song(String artist, String title) throws Exception {
        // given
        ErrorCode accessDeniedError = ErrorCode.ACCESS_DENIED_ERROR;

        // when
        ResultActions results = mockMvc.perform(MockMvcRequest.get("/api/songs")
                                                              .param("artist", artist)
                                                              .param("title", title));

        // then
        MockMvcResponse.FORBIDDEN(results, accessDeniedError)
                       .andDo(document("search-song-error",
                               responseFields(ResponseFields.error())
                       ))
        ;
    }

    private static Stream<Arguments> searchByInvalidParams() {
       return Stream.of(
               Arguments.of(StringUtils.repeat("Artist length is more than 30", 5), "Title"),
               Arguments.of("Artist", StringUtils.repeat("Title length is more than 30", 5))
       );
    }

    private static Stream<Arguments> searchManiaDbParameters() {
        return Stream.of(
                Arguments.of("Artist", "title")
        );
    }

    private static Stream<Arguments> searchLastFmApiParameters() {
        return Stream.of(
                Arguments.of("Artist Name", "Song Title", "http://imageUrl")
        );
    }

    private SearchApiResponse createMockManiaDbResponse() {
        Track firstTrack = Track.builder()
                                .artist("Artist 1")
                                .title("Title 1")
                                .imageUrl("http://imageUrl_1")
                                .build();
        Track secondTrack = Track.builder()
                                 .artist("Artist 2")
                                 .title("Title 2")
                                 .imageUrl("http://imageUrl_2")
                                 .build();
        Track thirdTrack = Track.builder()
                                .artist("Artist 3")
                                .title("Title 3")
                                .imageUrl("http://imageUrl_3")
                                .build();
        List<Track> tracks = Arrays.asList(firstTrack, secondTrack, thirdTrack);
        return SearchApiResponse.builder()
                                .totalCount(tracks.size())
                                .tracks(tracks)
                                .build();
    }
}
