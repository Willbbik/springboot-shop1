package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.BoardCommentDto;
import com.ecommerce.newshop1.dto.BoardDto;
import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.BoardComment;
import com.ecommerce.newshop1.service.BoardCommentService;
import com.ecommerce.newshop1.service.BoardService;
import com.ecommerce.newshop1.service.SecurityService;
import com.ecommerce.newshop1.utils.CommonService;
import com.ecommerce.newshop1.utils.PaginationShowSizeTen;
import com.ecommerce.newshop1.utils.ValidationSequence;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final BoardCommentService boardCommentService;
    private final CommonService commonService;
    private final SecurityService security;

    ModelMapper mapper = new ModelMapper();

    @GetMapping("/board/freeBoard")
    @ApiOperation(value = "자유게시판 목록 페이지")
    public String freeBoard(@RequestParam(name = "page", defaultValue = "1") int curPage, Model model){

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

    @GetMapping("/board/reComment/write/{parentId}")
    @ApiOperation(value = "대댓글 작성 페이지")
    public String reCommentWrite(@PathVariable(name = "parentId") Long parentId, Model model){

        model.addAttribute("parentId", parentId);

        return "board/board_reCommentWrite";
    }

    @GetMapping("/board/view/{boardId}")
    @ApiOperation(value = "게시글 상세보기 페이지")
    public String boardDetails(@PathVariable(name = "boardId") Long boardId, Model model){

        Board board = boardService.findById(boardId);
        BoardDto boardDto = boardService.editBoardDto(mapper.map(board, BoardDto.class));

        Long totalComment = boardCommentService.countByBoard(board);

        model.addAttribute("totalComment", totalComment);
        model.addAttribute("board", boardDto);

        return "board/board_view";
    }


    @GetMapping("/board/commentList")
    @ApiOperation(value = "게시글 댓글 리스트가 담긴 html 반환")
    public String getCommentList(@RequestParam(name = "boardId") Long boardId,
                                 @RequestParam(name = "lastCommentId", required = false) Long lastCommentId,
                                 @RequestParam(name = "more", required = false) String more, Model model){

        Board board = boardService.findById(boardId);
        Long totalComment =  boardCommentService.countByBoard(board);
        Pageable pageable = PageRequest.ofSize(10);

        List<BoardCommentDto> commentList = boardCommentService.searchAll(board, lastCommentId, pageable);
        List<BoardCommentDto> reCommentList = boardCommentService.searchAll(commentList);
        lastCommentId =  boardCommentService.getLastCommentId(commentList, lastCommentId);

        model.addAttribute("commentList", commentList);
        model.addAttribute("reCommentList", reCommentList);
        model.addAttribute("totalComment", totalComment);
        model.addAttribute("lastCommentId", lastCommentId);

        if(more != null) return "board/tab/board_commentListMore";
        return "board/tab/board_commentList";
    }


    @GetMapping("/board/comment/manage/{commentId}")
    @ApiOperation(value = "댓글 수정 팝업 페이지", notes = "팝업에 띄워줄 페이지")
    public String boardComment(@PathVariable(name = "commentId") Long commentId, Model model){

        BoardComment boardComment = boardCommentService.findById(commentId);
        BoardCommentDto boardCommentDto = mapper.map(boardComment, BoardCommentDto.class);

        model.addAttribute("content", boardCommentDto.getContent());
        return "board/board_commentUpdate";
    }


    @PostMapping("/board/write")
    @ApiOperation(value = "게시글 작성")
    public @ResponseBody String boardWrite(@Validated(ValidationSequence.class) BoardDto boardDto, BindingResult errors, Principal principal){

        if(errors.hasErrors()) return commonService.getErrorMessage(errors);
        if(!security.isAuthenticated()) return "login";


        boardService.save(boardDto, principal.getName());

        return "success";
    }

    @PostMapping("/board/comment/write")
    @ApiOperation(value = "게시글 댓글 저장")
    public @ResponseBody String boardCommentWrite(@Validated(ValidationSequence.class) BoardCommentDto boardCommentDto, BindingResult errors, Long boardId, Principal principal){

        if(!security.isAuthenticated()) return "login";
        if(errors.hasErrors()) return commonService.getErrorMessage(errors);

        boardCommentService.saveComment(boardCommentDto, boardId, principal.getName());
        return "success";

    }

    @PostMapping("/board/reComment/write")
    @ApiOperation(value = "게시글 대댓글 저장")
    public @ResponseBody String reCommentWrite(@Validated(ValidationSequence.class) BoardCommentDto boardCommentDto, BindingResult errors, Principal principal){

        if(!security.isAuthenticated()) return "login";
        if(errors.hasErrors()) return commonService.getErrorMessage(errors);


        boardCommentService.saveReComment(boardCommentDto, principal.getName());
        return "success";

    }





}
