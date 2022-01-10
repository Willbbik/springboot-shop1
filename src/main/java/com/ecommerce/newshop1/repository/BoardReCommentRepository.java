package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.BoardReComment;
import com.ecommerce.newshop1.repository.custom.BoardReCommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReCommentRepository extends JpaRepository<BoardReComment, Long>, BoardReCommentRepositoryCustom {
}
