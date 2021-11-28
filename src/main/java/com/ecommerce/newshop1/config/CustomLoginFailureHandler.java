package com.ecommerce.newshop1.config;

import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if(exception instanceof AuthenticationServiceException) {
            request.setAttribute("LoginFailureMessage", "죄송합니다, 시스템에 오류가 발생했습니다.");
        }
        else if(exception instanceof BadCredentialsException) {
            request.setAttribute("LoginFailureMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        setDefaultFailureUrl("/login");

        super.onAuthenticationFailure(request, response, exception);

    }
}
