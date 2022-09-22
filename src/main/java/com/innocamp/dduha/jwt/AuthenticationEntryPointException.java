package com.innocamp.dduha.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innocamp.dduha.dto.ResponseDto;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.innocamp.dduha.exception.ErrorCode.*;

// 인증이 필요함(401에러)
@Component
public class AuthenticationEntryPointException implements
        AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                new ObjectMapper().writeValueAsString(
                        ResponseDto.fail(NEED_LOGIN)
                )
        );
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
