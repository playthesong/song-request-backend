package com.requestrealpiano.songrequest.controller;

import com.requestrealpiano.songrequest.global.error.response.ErrorCode;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MockMvcResponse {

    // OK
    public static ResultActions OK(ResultActions resultActions) throws Exception {
        return resultActions.andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("success").value(true))
                            .andExpect(jsonPath("statusMessage").value("OK"))
                            .andExpect(jsonPath("data").isNotEmpty());
    }

    // BAD REQUEST
    public static ResultActions BAD_REQUEST(ResultActions resultActions, ErrorCode errorCode) throws Exception {
        return resultActions.andDo(print())
                            .andExpect(status().isBadRequest())
                            .andExpect(jsonPath("statusCode").value(errorCode.getStatusCode()))
                            .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    // UNAUTHORIZED
    public static ResultActions UNAUTHORIZED(ResultActions resultActions, ErrorCode errorCode) throws Exception {
        return resultActions.andDo(print())
                            .andExpect(status().isUnauthorized())
                            .andExpect(jsonPath("statusCode").value(errorCode.getStatusCode()))
                            .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    // FORBIDDEN
    public static ResultActions FORBIDDEN(ResultActions resultActions, ErrorCode errorCode) throws Exception {
        return resultActions.andDo(print())
                            .andExpect(status().isForbidden())
                            .andExpect(jsonPath("statusCode").value(errorCode.getStatusCode()))
                            .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }
}
