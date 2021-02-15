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
        List<LetterResponse> letterResponses = createMockLetterResponses();
        LetterResponse firstLetterResponse = letterResponses.get(0);
        SongSummary song = firstLetterResponse.getSong();
        AccountSummary account = firstLetterResponse.getAccount();
        LocalDateTime createdDateTime = firstLetterResponse.getCreatedDateTime();

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
                     .andExpect(jsonPath("data[0].id").value(firstLetterResponse.getId()))
                     .andExpect(jsonPath("data[0].songStory").value(firstLetterResponse.getSongStory()))
                     .andExpect(jsonPath("data[0].requestStatus").value(firstLetterResponse.getRequestStatus()))
                     .andExpect(jsonPath("data[0].createdDateTime").value(createdDateTime.toString()))
                     .andExpect(jsonPath("data[0].song.id").value(song.getId()))
                     .andExpect(jsonPath("data[0].song.title").value(song.getTitle()))
                     .andExpect(jsonPath("data[0].song.artist").value(song.getArtist()))
                     .andExpect(jsonPath("data[0].song.imageUrl").value(song.getImageUrl()))
                     .andExpect(jsonPath("data[0].account.id").value(account.getId()))
                     .andExpect(jsonPath("data[0].account.name").value(account.getName()))
                     .andExpect(jsonPath("data[0].account.avatarUrl").value(account.getAvatarUrl()))
        ;
    }

    @Test
    @DisplayName("OK - 유효한 ID 로부터 Letter 상세정보를 조회하는 API 테스트")
    void find_letter_by_valid_id() throws Exception {
        // given
        List<LetterResponse> letterResponses = createMockLetterResponses();
        LetterResponse letterResponse = letterResponses.get(0);
        SongSummary song = letterResponse.getSong();
        AccountSummary account = letterResponse.getAccount();
        Long letterId = letterResponse.getId();
        LocalDateTime createdDateTime = letterResponse.getCreatedDateTime();

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
                     .andExpect(jsonPath("data.id").value(letterResponse.getId()))
                     .andExpect(jsonPath("data.songStory").value(letterResponse.getSongStory()))
                     .andExpect(jsonPath("data.requestStatus").value(letterResponse.getRequestStatus()))
                     .andExpect(jsonPath("data.createdDateTime").value(createdDateTime.toString()))
                     .andExpect(jsonPath("data.song.id").value(song.getId()))
                     .andExpect(jsonPath("data.song.title").value(song.getTitle()))
                     .andExpect(jsonPath("data.song.artist").value(song.getArtist()))
                     .andExpect(jsonPath("data.song.imageUrl").value(song.getImageUrl()))
                     .andExpect(jsonPath("data.account.id").value(account.getId()))
                     .andExpect(jsonPath("data.account.name").value(account.getName()))
                     .andExpect(jsonPath("data.account.avatarUrl").value(account.getAvatarUrl()))
        ;
    }

    @ParameterizedTest
    @MethodSource("createNewLetterParameters")
    @DisplayName("새로운 Letter 생성 API 테스트")
    void create_new_Letter(String songStory, SongRequest songRequest, Long accountId) throws Exception {
        // given
        NewLetterRequest newLetterRequest = NewLetterRequestBuilder.newBuilder()
                                                                   .songStory(songStory)
                                                                   .songRequest(songRequest)
                                                                   .accountId(accountId)
                                                                   .build();
        List<LetterResponse> responses = createMockLetterResponses();
        LetterResponse response = responses.get(0);
        SongSummary song = response.getSong();
        LocalDateTime createdDateTime = response.getCreatedDateTime();
        AccountSummary account = response.getAccount();
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
                     .andExpect(jsonPath("data.id").value(response.getId()))
                     .andExpect(jsonPath("data.songStory").value(response.getSongStory()))
                     .andExpect(jsonPath("data.requestStatus").value(response.getRequestStatus()))
                     .andExpect(jsonPath("data.createdDateTime").value(createdDateTime.toString()))
                     .andExpect(jsonPath("data.song.id").value(song.getId()))
                     .andExpect(jsonPath("data.song.title").value(song.getTitle()))
                     .andExpect(jsonPath("data.song.artist").value(song.getArtist()))
                     .andExpect(jsonPath("data.song.imageUrl").value(song.getImageUrl()))
                     .andExpect(jsonPath("data.account.id").value(account.getId()))
                     .andExpect(jsonPath("data.account.name").value(account.getName()))
                     .andExpect(jsonPath("data.account.avatarUrl").value(account.getAvatarUrl()))
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


    private List<LetterResponse> createMockLetterResponses() {
        LetterResponse firstLetterResponse = LetterResponse.builder()
                                                           .id(1L)
                                                           .songStory("Song Story 1")
                                                           .requestStatus(RequestStatus.WAITING.getKey())
                                                           .createdDateTime(LocalDateTime.of(2021, 2, 14, 8, 43, 1))
                                                           .accountSummary(AccountSummary.builder()
                                                                                         .id(1L)
                                                                                         .name("Name 1")
                                                                                         .avatarUrl("http://avatarUrl_1")
                                                                                         .build())
                                                           .songSummary(SongSummary.builder()
                                                                                   .id(1L)
                                                                                   .title("Song Title 1")
                                                                                   .artist("Artist 1")
                                                                                   .build())
                                                           .build();

        LetterResponse secondLetterResponse = LetterResponse.builder()
                                                            .id(2L)
                                                            .songStory("Song Story 2")
                                                            .requestStatus(RequestStatus.WAITING.getKey())
                                                            .createdDateTime(LocalDateTime.of(2021, 2, 14, 8, 43, 1))
                                                            .accountSummary(AccountSummary.builder()
                                                                    .id(2L)
                                                                    .name("Name 2")
                                                                    .avatarUrl("http://avatarUrl_2")
                                                                    .build())
                                                            .songSummary(SongSummary.builder()
                                                                    .id(2L)
                                                                    .title("Song Title 2")
                                                                    .artist("Artist 2")
                                                                    .build())
                                                            .build();

        LetterResponse thirdLetterResponse = LetterResponse.builder()
                                                           .id(3L)
                                                           .songStory("Song Story 3")
                                                           .requestStatus(RequestStatus.WAITING.getKey())
                                                           .createdDateTime(LocalDateTime.of(2021, 2, 14, 8, 43, 1))
                                                           .accountSummary(AccountSummary.builder()
                                                                   .id(3L)
                                                                   .name("Name 3")
                                                                   .avatarUrl("http://avatarUrl_3")
                                                                   .build())
                                                           .songSummary(SongSummary.builder()
                                                                   .id(3L)
                                                                   .title("Song Title 3")
                                                                   .artist("Artist 3")
                                                                   .build())
                                                           .build();
        return Arrays.asList(firstLetterResponse, secondLetterResponse, thirdLetterResponse);
    }
}
