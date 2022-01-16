package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ItemRepositoryCustom {

    Long searchTotal(SearchDto searchDto);

    List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable);

    List<ItemDto> searchAllNoOffset(String category, Long id);

    List<ItemDto> searchAllBySort(String itemName, String sort, String value, Pageable pageable);

}
