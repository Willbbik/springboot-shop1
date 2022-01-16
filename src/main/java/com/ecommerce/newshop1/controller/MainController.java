package com.ecommerce.newshop1.controller;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {


    @GetMapping("/")
    @ApiOperation(value = "웹 기본 페이지")
    public String index() {
        return "index";
    }


    @GetMapping("/search")
    @ApiOperation(value = "검색 결과 페이지")
    public String search(){

        return "search";
    }




}
