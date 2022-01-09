package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.BoardDto;
import com.ecommerce.newshop1.service.BoardService;
import com.ecommerce.newshop1.service.SecurityService;
import com.ecommerce.newshop1.utils.CommonService;
import com.ecommerce.newshop1.utils.PaginationShowSizeTen;
import com.ecommerce.newshop1.utils.ValidationSequence;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommonService commonService;
    private final SecurityService security;


    @GetMapping("/board/freeBoard")
    @ApiOperation(value = "자유게시판 목록 페이지")
    public String notice(@RequestParam(name = "page", defaultValue = "1") int curPage, Model model){

        Long total =  boardService.count();
        PaginationShowSizeTen page = new PaginationShowSizeTen(total, curPage);

        Pageable pageable = PageRequest.of(page.getCurPage(), page.getShowMaxSize());
        List<BoardDto> boardList = boardService.searchAll(pageable);

        model.addAttribute("boardList", boardList);
        model.addAttribute("page", page);
        return "board/board_freeBoard";
    }

    @GetMapping("/board/write")
    @ApiOperation(value = "게시글 작성 페이지")
    public String boardWritePage(){

        return "board/board_write";
    }

    @GetMapping("/board/view")
    @ApiOperation(value = "게시글 상세보기 페이지")
    public String boardDetails(){

        return "board/board_view";
    }

    @GetMapping("/board/comment/manage/{commentId}")
    public String boardComment(@PathVariable(name = "commentId") Long commentId){



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
