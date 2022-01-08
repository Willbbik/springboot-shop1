package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.repository.custom.BoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
}
