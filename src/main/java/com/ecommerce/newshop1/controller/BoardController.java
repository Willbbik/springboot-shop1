package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.BoardDto;
import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.service.BoardService;
import com.ecommerce.newshop1.service.MemberService;
import com.ecommerce.newshop1.service.SecurityService;
import com.ecommerce.newshop1.utils.CommonService;
import com.ecommerce.newshop1.utils.ValidationSequence;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final CommonService commonService;
    private final SecurityService security;

    ModelMapper mapper = new ModelMapper();

    @GetMapping("/board/freeBoard")
    @ApiOperation(value = "자유게시판 목록 페이지")
    public String notice(){

        return "board/board_freeBoard";
    }

    @GetMapping("/board/write")
    @ApiOperation(value = "게시글 작성 페이지")
    public String boardWritePage(){

        return "board/board_write";
    }

    @PostMapping("/board/write")
    @ApiOperation(value = "게시글 작성")
    public @ResponseBody String boardWrite(@Validated(ValidationSequence.class) BoardDto boardDto, BindingResult errors, Principal principal){

        if(errors.hasErrors()) return commonService.getErrorMessage(errors);
        if(!security.isAuthenticated()) return "login";


        boardService.save(boardDto, principal.getName());

        return "success";
    }



}
