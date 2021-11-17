package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ItemRepositoryCustom {

    List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable);

    Long searchTotal(SearchDto searchDto);

    List<ItemDto> searchAllNoOffset(Long id);

}
