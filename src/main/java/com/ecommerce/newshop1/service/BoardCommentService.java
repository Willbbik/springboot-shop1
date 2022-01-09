package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.BoardCommentDto;

public interface BoardCommentService {

    BoardCommentDto findById(Long commentId);

}
