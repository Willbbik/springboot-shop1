package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.BoardDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {

    Long save(BoardDto boardDto, String userId);

    Long count();

    List<BoardDto> searchAll(Pageable pageable);
}
