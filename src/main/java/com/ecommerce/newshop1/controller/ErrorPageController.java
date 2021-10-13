package com.ecommerce.newshop1.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class ErrorPageController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping("/error")
    public String Error(HttpServletRequest request, Model model){

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(status != null){
            Integer statusCode = Integer.parseInt(status.toString());
            if(statusCode == HttpStatus.SC_NOT_FOUND){

                return "error/404";
            }else if(statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR){
                return "error/500";
            } else{
                return "error/error";
            }
        }

        return "error/error";
    }








}
