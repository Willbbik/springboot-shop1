package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.BoardDto;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class BoardController {


    @GetMapping("/board/list/all")
    public String listAll(){
        return "board/listAll";
    }


    @GetMapping("/board/write/notice")
    public String write(){
        return "board/writenotice";
    }


    @ApiOperation(value = "공지사항 저장")
    @PostMapping("/board/write/notice")
    public String writeNotice(BoardDto boardDto){

        System.out.println(boardDto.getSubject());
        return "redirect:/board/write/notice";
    }




}
