package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.BoardCommentDto;
import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.BoardComment;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.exception.BoardCommentNotFoundException;
import com.ecommerce.newshop1.repository.BoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardCommentServiceImpl implements BoardCommentService{

    private final BoardCommentRepository boardCommentRepository;
    private final MemberService memberService;
    private final BoardService boardService;

    ModelMapper mapper = new ModelMapper();

    @Override
    @Transactional(readOnly = true)
    public Long countByBoard(Board board) {
        return boardCommentRepository.countByBoard(board);
    }

    @Override
    @Transactional(readOnly = true)
    public BoardComment findById(Long commentId) {

        return boardCommentRepository.findById(commentId)
                    .orElseThrow(() -> new BoardCommentNotFoundException("존재하지 않는 댓글 번호입니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardCommentDto> searchAll(Board board, Long lastCommentId) {
        return boardCommentRepository.searchAll(board, lastCommentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardCommentDto> searchAll(List<BoardCommentDto> boardCmtList) {

        List<BoardCommentDto> boardCommentResultList = new ArrayList<>();
        for(BoardCommentDto dto : boardCmtList){
            List<BoardCommentDto> result = boardCommentRepository.searchAll(dto.getId());
            boardCommentResultList.addAll(result);
        }

        return boardCommentResultList;
    }

    @Override
    @Transactional
    public Long saveComment(BoardCommentDto boardCommentDto, Long boardId, String userId) {

        Member member = memberService.findByUserId(userId);
        Board board = boardService.findById(boardId);

        BoardComment boardComment = mapper.map(boardCommentDto, BoardComment.class);
        boardComment.setWriter(member.getUserId().substring(0, 3) + "***");
        member.addBoardCommentList(boardComment);
        board.addBoardCommentList(boardComment);

        return boardCommentRepository.save(boardComment).getId();
    }


    @Override
    public Long saveComment(BoardCommentDto boardCommentDto, Long boardId, String userId, Long parent) {
        return null;
    }



}
