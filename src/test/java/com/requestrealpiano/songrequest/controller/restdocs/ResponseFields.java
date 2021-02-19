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
                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("API 요청 성공 여부"),
                fieldWithPath("statusMessage").type(JsonFieldType.STRING).description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터")
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

    // Account
    public static List<FieldDescriptor> account() {
        return List.of(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 고유 ID"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),
                fieldWithPath("avatarUrl").type(JsonFieldType.STRING).description("사용자 프로필 이미지")
        );
    }
}
