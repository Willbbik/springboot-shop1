package com.ecommerce.newshop1.controller;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BoardController {

    @GetMapping("/board/freeBoard")
    @ApiOperation(value = "공지사항 페이지")
    public String notice(){

        return "board/board_freeBoard";
    }




}
