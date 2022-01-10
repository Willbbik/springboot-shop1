package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.BoardCommentDto;
import com.ecommerce.newshop1.dto.BoardReCommentDto;

import java.util.List;

public interface BoardReCommentService {

    List<BoardReCommentDto> searchAll(List<BoardCommentDto> commentList);

    Long save(BoardReCommentDto reCommentDto, Long commentId, String userId);

}
