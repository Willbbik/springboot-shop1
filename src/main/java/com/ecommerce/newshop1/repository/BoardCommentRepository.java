package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.BoardComment;
import com.ecommerce.newshop1.repository.custom.BoardCommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long>, BoardCommentRepositoryCustom {
}
