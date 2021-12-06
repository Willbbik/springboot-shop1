package com.ecommerce.newshop1.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class BoardController {

    @GetMapping("/board/list/all")
    public String listAll(){
        return "board/listAll";
    }



}
