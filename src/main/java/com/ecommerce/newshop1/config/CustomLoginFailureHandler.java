package com.ecommerce.newshop1.config;

import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        setDefaultFailureUrl("/login");

        if(exception instanceof BadCredentialsException) {
            request.setAttribute("LoginFailureMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        request.setAttribute("userId", request.getParameter("userId"));
        request.setAttribute("password", request.getParameter("password"));

        request.getRequestDispatcher("/login").forward(request, response);
    }
}
