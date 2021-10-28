package com.ecommerce.newshop1.utils;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    String anonymousUser = "anonymousUser";

    public boolean isAuthenticated(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return !authentication.getPrincipal().equals(anonymousUser);
    }

}
