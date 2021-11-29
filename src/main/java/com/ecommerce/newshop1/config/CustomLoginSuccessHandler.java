package com.ecommerce.newshop1.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    // 로깅
    private static final Logger logger = LoggerFactory.getLogger(CustomLoginSuccessHandler.class);

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public CustomLoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }

//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
//        HttpSession session = request.getSession();
//        if (session != null){
//            String redirectUrl = (String) session.getAttribute("prevPage");
//
//            if(redirectUrl != null){ // 이전 페이지가 존재한다면
//                session.removeAttribute("prevPage");
//                if(redirectUrl.equals("http://localhost:8080/login")) redirectUrl = "/";
//
//                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
//            }else{
//                super.onAuthenticationSuccess(request, response, authentication);
//            }
//        } else{
//            super.onAuthenticationSuccess(request, response, authentication);
//        }
//    }


//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
//
//        clearAuthenticationAttributes(request);
//
//        resultRedirectStrategy(request, response, authentication);
//    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if ( savedRequest != null ) {

            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            HttpSession session = request.getSession();
            if (session != null){
                String redirectUrl = (String) session.getAttribute("prevPage");

                if(redirectUrl != null){ // 이전 페이지가 존재한다면
                    session.removeAttribute("prevPage");
                    if(redirectUrl.equals("http://localhost:8080/login")) redirectUrl = "/";

                    getRedirectStrategy().sendRedirect(request, response, redirectUrl);
                }else{
                    super.onAuthenticationSuccess(request, response, authentication);
                }
            } else{
                super.onAuthenticationSuccess(request, response, authentication);
            }
        }

    }

    // redirectUrl 지정 메서드
    protected void resultRedirectStrategy(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if ( savedRequest != null ) {
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        }else {
            redirectStrategy.sendRedirect(request, response, "/");
        }
    }


}
