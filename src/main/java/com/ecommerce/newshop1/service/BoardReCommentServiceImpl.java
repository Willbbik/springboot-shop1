package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.BoardCommentDto;
import com.ecommerce.newshop1.dto.BoardReCommentDto;
import com.ecommerce.newshop1.dto.CommentPostDto;
import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.BoardComment;
import com.ecommerce.newshop1.entity.BoardReComment;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.exception.BoardCommentNotFoundException;
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
    private final ModelMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public BoardReComment findById(Long reCommentId) {
        return boardReCommentRepository.findById(reCommentId)
                .orElseThrow(() -> new BoardCommentNotFoundException("존재하지 않는 댓글입니다."));
    }

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

    @Override
    @Transactional
    public void deleteById(Long reCommentId) {

        boardReCommentRepository.deleteById(reCommentId);
    }

    @Override
    @Transactional
    public void updateReComment(Long reCommentId, CommentPostDto postDto) {

        BoardReComment reComment = boardReCommentRepository.findById(reCommentId)
                 .orElseThrow(() -> new BoardCommentNotFoundException("존재하지 않는 댓글입니다."));

        reComment.setContent(postDto.getContent());
        reComment.setHide(postDto.getHide());
    }
}
