package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.BoardCommentDto;
import com.ecommerce.newshop1.dto.BoardDto;
import com.ecommerce.newshop1.dto.BoardReCommentDto;
import com.ecommerce.newshop1.dto.CommentPostDto;
import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.BoardComment;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.service.*;
import com.ecommerce.newshop1.utils.CommonService;
import com.ecommerce.newshop1.utils.PaginationShowSizeTen;
import com.ecommerce.newshop1.utils.ValidationSequence;
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
    private final BoardReCommentService boardReCommentService;
    private final CommonService commonService;
    private final SecurityService security;

    private final ModelMapper mapper;

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

    @GetMapping("/board/reComment/write/{commentId}")
    @ApiOperation(value = "대댓글 작성 페이지")
    public String reCommentWrite(@PathVariable(name = "commentId") Long commentId, Model model){

        model.addAttribute("commentId", commentId);

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
    @ApiOperation(value = "게시글 댓글 리스트가 담긴 html 반환", notes = "게시글 상세보기 진입시 자동 호출")
    public String getCommentList(@RequestParam(name = "boardId") Long boardId,
                                 @RequestParam(name = "lastCommentId", required = false) Long lastCommentId,
                                 @RequestParam(name = "more", required = false) String more, Model model){

        Board board = boardService.findById(boardId);
        Long totalComment =  boardCommentService.countByBoard(board);
        Pageable pageable = PageRequest.ofSize(10);

        List<BoardCommentDto> commentList = boardCommentService.searchAll(board, lastCommentId, pageable);
        List<BoardReCommentDto> reCommentList = boardReCommentService.searchAll(commentList);
        lastCommentId =  boardCommentService.getLastCommentId(commentList, lastCommentId);

        model.addAttribute("commentList", commentList);
        model.addAttribute("reCommentList", reCommentList);
        model.addAttribute("totalComment", totalComment);
        model.addAttribute("lastCommentId", lastCommentId);

        if(more != null) return "board/tab/board_commentListMore";
        return "board/tab/board_commentList";
    }


    @GetMapping("/board/comment/{commentId}")
    @ApiOperation(value = "댓글 수정 팝업 페이지", notes = "팝업에 띄워줄 페이지")
    public String boardComment(@PathVariable(name = "commentId") Long commentId, Model model){

        BoardComment boardComment = boardCommentService.findById(commentId);

        model.addAttribute("commentId", commentId);
        model.addAttribute("content", boardComment.getContent());
        model.addAttribute("hide", boardComment.getHide());
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
    public @ResponseBody String reCommentWrite(@Validated(ValidationSequence.class) BoardReCommentDto reCommentDto, BindingResult errors, Principal principal){

        if(!security.isAuthenticated()) return "login";
        if(errors.hasErrors()) return commonService.getErrorMessage(errors);

        boardReCommentService.save(reCommentDto, reCommentDto.getCommentId(), principal.getName());
        return "success";
    }

    @PatchMapping("/board/comment")
    @ApiOperation(value = "댓글 내용 수정")
    public @ResponseBody String commentUpdate(@Validated(ValidationSequence.class) CommentPostDto postDto, BindingResult errors, Principal principal){

        if(!security.isAuthenticated()) return "login";
        if(errors.hasErrors()) return commonService.getErrorMessage(errors);

        BoardComment comment = boardCommentService.findById(postDto.getId());
        BoardCommentDto commentDto = mapper.map(comment, BoardCommentDto.class);

        return "success";


    }

    @DeleteMapping("/board/comment")
    @ApiOperation(value = "댓글 삭제")
    public @ResponseBody String deleteComment(@RequestParam(name = "commentId") Long commentId, Principal principal){

        if(!security.isAuthenticated()) return "login";

        // 작성자 비교
        BoardComment comment = boardCommentService.findById(commentId);
        Member member = comment.getMember();
        if(!member.getUserId().equals(principal.getName())) return "role";
        // if(!comment.getMember().getUserId().equals(principal.getName())) return "role";

        boardCommentService.deleteComment(commentId);
        return "success";
    }


}
