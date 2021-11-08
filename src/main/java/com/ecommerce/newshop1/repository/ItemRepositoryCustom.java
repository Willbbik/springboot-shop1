package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<ItemDto> searchAll(SearchDto searchDto, Pageable pageable);

}
