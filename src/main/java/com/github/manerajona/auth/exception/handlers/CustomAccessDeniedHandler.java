package com.github.manerajona.auth.exception.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.manerajona.auth.exception.error.ErrorCode;
import com.github.manerajona.auth.exception.error.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(getBody());
    }

    private static String getBody() throws JsonProcessingException {
        ErrorDetails error = ErrorDetails.builder()
                .code(ErrorCode.ROLE_INVALID)
                .detail(ErrorCode.ROLE_INVALID.getDefaultMessage())
                .build();
        return MAPPER.writeValueAsString(error);
    }
}
