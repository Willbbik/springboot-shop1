package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.BoardDto;
import com.ecommerce.newshop1.entity.Board;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {


    Long count();

    List<BoardDto> searchAll(Pageable pageable);

    Board findById(Long boardId);

    Long save(BoardDto boardDto, String userId);

    Long update(Long boardId, BoardDto boardDto);

    Long updateHide(Long boardId);

    void deleteById(Long boardId);

}
