package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.BoardCommentDto;
import com.ecommerce.newshop1.entity.BoardComment;

public interface BoardCommentService {

    BoardComment findById(Long commentId);

    Long saveComment(BoardCommentDto boardCommentDto, Long boardId, String userId);

    Long saveComment(BoardCommentDto boardCommentDto, Long boardId, String userId, Long parent);

}
