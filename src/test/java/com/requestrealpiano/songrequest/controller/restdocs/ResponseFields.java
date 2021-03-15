package com.requestrealpiano.songrequest.controller.restdocs;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

public class ResponseFields {

    // Common
    public static List<FieldDescriptor> common() {
        return List.of(
                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                fieldWithPath("data").type(JsonFieldType.VARIES).description("응답 데이터")
        );
    }

    // Error
    public static List<FieldDescriptor> error() {
        return List.of(
                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("에러 상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지"),
                fieldWithPath("errors[]").type(JsonFieldType.ARRAY).description("에러 상세 내역"),
                fieldWithPath("errors[].value").type(JsonFieldType.STRING).description("에러 발생 필드 값").optional(),
                fieldWithPath("errors[].reason").type(JsonFieldType.STRING).description("에러 발생 원인").optional()
        );
    }

    // Letter
    public static List<FieldDescriptor> letter() {
        return List.of(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("신청곡 ID"),
                fieldWithPath("songStory").type(JsonFieldType.STRING).description("사연 내용"),
                fieldWithPath("requestStatus").type(JsonFieldType.STRING).description("신청곡 요청 상태"),
                fieldWithPath("createdDateTime").type(JsonFieldType.STRING).description("신청곡 등록 일시"),
                subsectionWithPath("song").type(JsonFieldType.OBJECT).description("신청곡 음원 정보"),
                subsectionWithPath("account").type(JsonFieldType.OBJECT).description("신청자 정보")
        );
    }

    // Song
    public static List<FieldDescriptor> song() {
        return List.of(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("음원 ID"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("음원 제목"),
                fieldWithPath("artist").type(JsonFieldType.STRING).description("아티스트"),
                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("앨범 이미지")
        );
    }

    public static List<FieldDescriptor> searchSongResult() {
        return List.of(
                fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("검색 결과 수"),
                fieldWithPath("tracks[]").type(JsonFieldType.ARRAY).description("검색 결과 목록"),
                fieldWithPath("tracks[].title").type(JsonFieldType.STRING).description("음원 제목"),
                fieldWithPath("tracks[].artist").type(JsonFieldType.STRING).description("아티스트"),
                fieldWithPath("tracks[].imageUrl").type(JsonFieldType.STRING).description("앨범 이미지")
        );
    }

    // Account
    public static List<FieldDescriptor> account() {
        return List.of(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 고유 ID"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),
                fieldWithPath("avatarUrl").type(JsonFieldType.STRING).description("사용자 프로필 이미지")
        );
    }
}
