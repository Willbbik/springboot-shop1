package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.BoardDto;

public interface BoardService {

    Long save(BoardDto boardDto, String userId);

}
