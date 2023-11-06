package com.iso.spring3.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iso.spring3.core.utils.constants.CoreConstants;
import com.iso.spring3.core.utils.result.ErrorResult;
import com.iso.spring3.exceptions.CustomRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RequiredArgsConstructor
@Component
public class CustomAccessDeniedExceptionHandler implements AccessDeniedHandler {
    private final ObjectMapper jsonMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException
    ) throws RuntimeException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        ErrorResult result = ErrorResult.of(CoreConstants.ACCESS_DENIED);
        try {
            response.getWriter().write(jsonMapper.writeValueAsString(result));
        } catch (Exception e) {
            throw new CustomRuntimeException(e.getMessage(), e);
        }


    }
}


