package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.BoardCommentDto;
import com.ecommerce.newshop1.dto.BoardReCommentDto;

import java.util.List;

public interface BoardReCommentRepositoryCustom {

    List<BoardReCommentDto> searchAll(BoardCommentDto comment);

}
