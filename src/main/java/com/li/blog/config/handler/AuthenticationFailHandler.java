package com.li.blog.config.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author li
 * @version 1.0
 * @since 18-9-21 上午10:35
 **/
@Component
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {
    private static final String FORM_SUBMIT = "application/x-www-form-urlencoded";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String contentType = request.getHeader("Content-Type");
        //判断是否表单提交
        if (FORM_SUBMIT.equals(contentType)) {
            response.sendRedirect("/auth/login-error");
        } else {
            //基于ajax提交
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
        }
    }
}
