package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.BoardDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    List<BoardDto> searchAll(Pageable pageable);

}
