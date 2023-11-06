package com.iso.spring3.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iso.spring3.core.utils.constants.CoreConstants;
import com.iso.spring3.core.utils.result.SuccessResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final ObjectMapper jsonMapper;

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_OK);
        SuccessResult result = SuccessResult.of(CoreConstants.LOGOUT_SUCCESS);
        response.setContentType("application/json");
        response.getWriter().write(jsonMapper.writeValueAsString(result));
    }
}
