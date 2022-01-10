package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.BoardCommentDto;
import com.ecommerce.newshop1.entity.Board;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardCommentRepositoryCustom {

    List<BoardCommentDto> searchAll(Board board, Long lastCommentId, Pageable pageable);

    List<BoardCommentDto> searchAll(Long parent);

}
