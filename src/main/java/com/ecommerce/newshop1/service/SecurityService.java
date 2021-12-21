package com.ecommerce.newshop1.service;


import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    // 로그인 했는지 안했는지 검사
    public boolean isAuthenticated(){

        AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
        return (!trustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication()));
    }

    // 로그인한 사용자 아이디 가져오기
    public String getName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }


    // 아이디 검사
    public boolean compareName(String username){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName().equals(username);
    }


    // 파라미터로 들어온 권한 가지고 있는지 검사
    public boolean checkHasRole(String role){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().contains(new SimpleGrantedAuthority(role));
    }





}
