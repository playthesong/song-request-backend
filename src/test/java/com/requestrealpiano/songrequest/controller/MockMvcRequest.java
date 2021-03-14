package com.requestrealpiano.songrequest.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class MockMvcRequest {

    // GET
    public static MockHttpServletRequestBuilder get(String url) {
        return MockMvcRequestBuilders.get(url)
                                     .accept(MediaType.APPLICATION_JSON);
    }

    public static MockHttpServletRequestBuilder get(String url, Long pathVariable) {
        return RestDocumentationRequestBuilders.get(url, pathVariable)
                                               .accept(MediaType.APPLICATION_JSON);
    }

    // POST
    public static MockHttpServletRequestBuilder post(String url, String content) {
        return MockMvcRequestBuilders.post(url)
                                     .accept(APPLICATION_JSON)
                                     .contentType(APPLICATION_JSON)
                                     .content(content);
    }

    public static MockHttpServletRequestBuilder post(String url, String content, String jwtToken) {
        return MockMvcRequestBuilders.post(url)
                                     .accept(APPLICATION_JSON)
                                     .contentType(APPLICATION_JSON)
                                     .content(content)
                                     .header(HttpHeaders.AUTHORIZATION, jwtToken);
    }
}
