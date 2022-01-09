package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.BoardCommentDto;
import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.BoardComment;

import java.util.List;

public interface BoardCommentService {

    Long countByBoard(Board board);

    BoardComment findById(Long commentId);

    Long saveComment(BoardCommentDto boardCommentDto, Long boardId, String userId);

    Long saveComment(BoardCommentDto boardCommentDto, Long boardId, String userId, Long parent);

    List<BoardCommentDto> searchAll(Board board, Long lastCommentId);

    List<BoardCommentDto> searchAll(List<BoardCommentDto> boardCommentList);

}
