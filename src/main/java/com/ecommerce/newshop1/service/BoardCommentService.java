package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.BoardCommentDto;
import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.BoardComment;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardCommentService {

    Long countByBoard(Board board);

    Long getLastCommentId(List<BoardCommentDto> commentList, Long lastCommentId);

    BoardComment findById(Long commentId);

    Long saveComment(BoardCommentDto boardCommentDto, Long boardId, String userId);

    List<BoardCommentDto> searchAll(Board board, Long lastCommentId, Pageable pageable);

    void deleteComment(Long commentId);

}
