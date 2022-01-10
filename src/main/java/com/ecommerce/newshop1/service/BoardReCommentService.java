package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.BoardCommentDto;
import com.ecommerce.newshop1.dto.BoardReCommentDto;
import com.ecommerce.newshop1.dto.CommentPostDto;
import com.ecommerce.newshop1.entity.BoardReComment;

import java.util.List;

public interface BoardReCommentService {

    BoardReComment findById(Long reCommentId);

    List<BoardReCommentDto> searchAll(List<BoardCommentDto> commentList);

    Long save(CommentPostDto postDto, String userId);

    void deleteById(Long reCommentId);

    void updateReComment(Long reCommentId, CommentPostDto postDto);

}
