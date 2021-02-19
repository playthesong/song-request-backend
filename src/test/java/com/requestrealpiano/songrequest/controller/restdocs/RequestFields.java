package com.requestrealpiano.songrequest.controller.restdocs;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class RequestFields {

    public static List<FieldDescriptor> createLetter() {
        return List.of(
                fieldWithPath("songStory").type(JsonFieldType.STRING).description("신청곡 사연 내용"),
                fieldWithPath("song").type(JsonFieldType.OBJECT).description("신청곡 정보"),
                fieldWithPath("song.title").type(JsonFieldType.STRING).description("신청곡 제목"),
                fieldWithPath("song.artist").type(JsonFieldType.STRING).description("아티스트"),
                fieldWithPath("song.imageUrl").type(JsonFieldType.STRING).description("앨범 이미지"),
                fieldWithPath("accountId").type(JsonFieldType.NUMBER).description("신청자 ID")
        );
    }
}
