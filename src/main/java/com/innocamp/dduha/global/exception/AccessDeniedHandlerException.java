package com.innocamp.dduha.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innocamp.dduha.global.common.ResponseDto;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.innocamp.dduha.global.exception.ErrorCode.*;

// 접근 권한 없음(403에러)
@Component
public class AccessDeniedHandlerException implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                new ObjectMapper().writeValueAsString(
                        ResponseDto.fail(NEED_LOGIN)
                )
        );
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
