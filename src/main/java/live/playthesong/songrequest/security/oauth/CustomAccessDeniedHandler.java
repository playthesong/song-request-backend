package live.playthesong.songrequest.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.playthesong.songrequest.global.error.response.ErrorCode;
import live.playthesong.songrequest.global.error.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("handleAccessDeniedException", accessDeniedException);
        ErrorCode accessDeniedError = ErrorCode.ACCESS_DENIED_ERROR;
        String errorResponse = objectMapper.writeValueAsString(ErrorResponse.from(accessDeniedError));
        response.getWriter().print(errorResponse);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(accessDeniedError.getStatusCode());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
