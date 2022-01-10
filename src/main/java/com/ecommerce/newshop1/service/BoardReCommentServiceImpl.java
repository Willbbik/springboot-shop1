package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.BoardCommentDto;
import com.ecommerce.newshop1.dto.BoardReCommentDto;
import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.BoardComment;
import com.ecommerce.newshop1.entity.BoardReComment;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.repository.BoardReCommentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardReCommentServiceImpl implements BoardReCommentService{

    private final BoardReCommentRepository boardReCommentRepository;
    private final BoardCommentService boardCommentService;
    private final MemberService memberService;

    ModelMapper mapper = new ModelMapper();

    @Override
    @Transactional(readOnly = true)
    public List<BoardReCommentDto> searchAll(List<BoardCommentDto> commentList) {

        List<BoardReCommentDto> reCommentList = new ArrayList<>();
        for(BoardCommentDto comment : commentList){
            reCommentList.addAll(boardReCommentRepository.searchAll(comment));
        }
        return reCommentList;
    }

    @Override
    @Transactional
    public Long save(BoardReCommentDto reCommentDto, Long commentId, String userId) {

        BoardComment comment = boardCommentService.findById(commentId);
        Member member = memberService.findByUserId(userId);
        Board board = comment.getBoard();

        BoardReComment reComment = mapper.map(reCommentDto, BoardReComment.class);
        reComment.setWriter(member.getUserId().substring(0, 3) + "***");
        comment.addBoardReCommentList(reComment);
        member.addBoardReCommentList(reComment);
        board.addBoardReCommentList(reComment);

        return boardReCommentRepository.save(reComment).getId();
    }

}
