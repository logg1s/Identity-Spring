package com.logistn.identity_service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistn.identity_service.dto.response.ApiResponse;
import com.logistn.identity_service.exception.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        ErrorMessage errorMessage = ErrorMessage.UNAUTHENTICATED;

        response.setStatus(errorMessage.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(errorMessage.getCode())
                .message(errorMessage.getMessage())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer();
    }
}
