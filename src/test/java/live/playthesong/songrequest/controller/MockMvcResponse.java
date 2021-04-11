package live.playthesong.songrequest.controller;

import live.playthesong.songrequest.global.error.response.ErrorCode;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MockMvcResponse {

    // OK
    public static ResultActions OK(ResultActions resultActions) throws Exception {
        return resultActions.andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("statusCode").value(200))
                            .andExpect(jsonPath("data").isNotEmpty());
    }

    // CREATED
    public static ResultActions CREATED(ResultActions resultActions) throws Exception {
        return resultActions.andDo(print())
                            .andExpect(status().isCreated())
                            .andExpect(jsonPath("statusCode").value(201))
                            .andExpect(jsonPath("data").isNotEmpty());
    }

    // NO CONTENT
    public static ResultActions NO_CONTENT(ResultActions resultActions) throws Exception {
        return resultActions.andDo(print())
                            .andExpect(status().isNoContent());
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
