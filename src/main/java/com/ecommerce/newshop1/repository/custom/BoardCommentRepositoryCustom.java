package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.BoardCommentDto;
import com.ecommerce.newshop1.entity.Board;


import java.util.List;

public interface BoardCommentRepositoryCustom {

    List<BoardCommentDto> searchAll(Board board, Long lastCommentId);

}
