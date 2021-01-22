package com.requestrealpiano.songrequest.controller;

import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import com.requestrealpiano.songrequest.domain.letter.dto.response.LetterResponse;
import com.requestrealpiano.songrequest.domain.letter.dto.response.inner.AccountSummary;
import com.requestrealpiano.songrequest.domain.letter.dto.response.inner.SongSummary;
import com.requestrealpiano.songrequest.service.LetterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class LetterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LetterService letterService;

    @Test
    @DisplayName("모든 Letter를 조회 한다.")
    void find_all_letters() throws Exception {
        // given
        List<LetterResponse> letterResponses = createMockLetterResponses();
        LetterResponse firstLetterResponse = letterResponses.get(0);
        SongSummary song = firstLetterResponse.getSong();
        AccountSummary account = firstLetterResponse.getAccount();

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
                     .andExpect(jsonPath("data[0].song.id").value(song.getId()))
                     .andExpect(jsonPath("data[0].song.title").value(song.getTitle()))
                     .andExpect(jsonPath("data[0].song.artist").value(song.getArtist()))
                     .andExpect(jsonPath("data[0].song.imageUrl").value(song.getImageUrl()))
                     .andExpect(jsonPath("data[0].account.id").value(account.getId()))
                     .andExpect(jsonPath("data[0].account.name").value(account.getName()))
                     .andExpect(jsonPath("data[0].account.avatarUrl").value(account.getAvatarUrl()))
                ;
    }

    private List<LetterResponse> createMockLetterResponses() {
        LetterResponse firstLetterResponse = LetterResponse.builder()
                                                           .id(1L)
                                                           .songStory("Song Story 1")
                                                           .requestStatus(RequestStatus.WAITING.getKey())
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
