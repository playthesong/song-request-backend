package com.requestrealpiano.songrequest.controller;

import com.requestrealpiano.songrequest.security.oauth.OAuthAccount;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class MockMvcRequest {

    private MockHttpServletRequestBuilder builder;

    public static MockMvcRequest get(String url) {
        MockMvcRequest request = new MockMvcRequest();
        request.builder = MockMvcRequestBuilders.get(url)
                                                .accept(MediaType.APPLICATION_JSON);
        return request;
    }

    public static MockMvcRequest get(String url, Object pathVariable) {
        MockMvcRequest request = new MockMvcRequest();
        request.builder = RestDocumentationRequestBuilders.get(url, pathVariable)
                                                          .accept(MediaType.APPLICATION_JSON);
        return request;
    }

    public static MockMvcRequest post(String url) {
        MockMvcRequest request = new MockMvcRequest();
        request.builder = MockMvcRequestBuilders.post(url)
                                                .accept(APPLICATION_JSON);
        return request;
    }

    public static MockMvcRequest put(String url, Object pathVariable) {
        MockMvcRequest request = new MockMvcRequest();
        request.builder = MockMvcRequestBuilders.put(url, pathVariable)
                                                .accept(APPLICATION_JSON);
        return request;
    }

    public static MockMvcRequest delete(String url) {
        MockMvcRequest request = new MockMvcRequest();
        request.builder = MockMvcRequestBuilders.delete(url)
                                                .accept(APPLICATION_JSON);
        return request;
    }

    public static MockMvcRequest delete(String url, Object pathVariable) {
        MockMvcRequest request = new MockMvcRequest();
        request.builder = MockMvcRequestBuilders.delete(url, pathVariable)
                                                .accept(APPLICATION_JSON);
        return request;
    }

    public MockMvcRequest withParam(String name, String value) {
        this.builder.param(name, value);
        return this;
    }

    public MockMvcRequest withBody(String requestBody) {
        this.builder.contentType(APPLICATION_JSON)
                    .content(requestBody);
        return this;
    }

    public MockMvcRequest withToken(String jwtToken) {
        this.builder.header(HttpHeaders.AUTHORIZATION, jwtToken);
        return this;
    }

    public MockMvcRequest withPrincipal(OAuthAccount loginAccount) {
        this.builder.principal(new UsernamePasswordAuthenticationToken(loginAccount, null,
                                                                       loginAccount.getAuthorities()));
        return this;
    }

    public MockHttpServletRequestBuilder doRequest() {
        return this.builder;
    }
}
