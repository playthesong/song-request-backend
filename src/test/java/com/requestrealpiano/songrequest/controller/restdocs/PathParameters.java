package com.requestrealpiano.songrequest.controller.restdocs;

import org.springframework.restdocs.request.ParameterDescriptor;

import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

public class PathParameters {

    // Letter
    public static ParameterDescriptor letterIdParameter() {
        return parameterWithName("id").description("신청곡 ID");
    }
}
