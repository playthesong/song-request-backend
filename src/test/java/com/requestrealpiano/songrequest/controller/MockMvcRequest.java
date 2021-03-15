package com.requestrealpiano.songrequest.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class MockMvcRequest {

    public static MockRequest get(String url) {
        MockRequest request = new MockRequest();
        request.mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(url)
                                                                      .accept(MediaType.APPLICATION_JSON);
        return request;
    }

    public static MockRequest get(String url, Long pathVariable) {
        MockRequest request = new MockRequest();
        request.mockHttpServletRequestBuilder = RestDocumentationRequestBuilders.get(url, pathVariable)
                                                                                .accept(MediaType.APPLICATION_JSON);
        return request;
    }

    public static MockRequest post(String url) {
        MockRequest request = new MockRequest();
        request.mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(url)
                                                                      .accept(APPLICATION_JSON);
        return request;
    }

    public static class MockRequest {

        private MockHttpServletRequestBuilder mockHttpServletRequestBuilder;

        private MockRequest() {
        }

        public MockRequest withParam(String name, String value) {
            this.mockHttpServletRequestBuilder.param(name, value);
            return this;
        }

        public MockRequest withBody(String requestBody) {
            this.mockHttpServletRequestBuilder.contentType(APPLICATION_JSON)
                                              .content(requestBody);
            return this;
        }

        public MockRequest withToken(String jwtToken) {
            this.mockHttpServletRequestBuilder.header(HttpHeaders.AUTHORIZATION, jwtToken);
            return this;
        }

        public MockHttpServletRequestBuilder doRequest() {
            return this.mockHttpServletRequestBuilder;
        }
    }
}
