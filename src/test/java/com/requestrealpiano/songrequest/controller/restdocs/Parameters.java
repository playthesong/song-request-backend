package com.requestrealpiano.songrequest.controller.restdocs;

import org.springframework.restdocs.request.ParameterDescriptor;

import java.util.List;

import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

public class Parameters {

    // Letter
    public static ParameterDescriptor letterId() {
        return parameterWithName("id").description("신청곡 ID");
    }

    // Song
    public static List<ParameterDescriptor> searchSong() {
        return List.of(
                parameterWithName("artist").description("아티스트 (30자 이하)"),
                parameterWithName("title").description("제목 (30자 이하)")
        );
    }
}
