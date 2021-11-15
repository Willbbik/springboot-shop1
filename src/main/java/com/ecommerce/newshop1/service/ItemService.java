package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.List;

public interface ItemService {

    List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable);

    Long searchTotal(SearchDto searchDto);

}
