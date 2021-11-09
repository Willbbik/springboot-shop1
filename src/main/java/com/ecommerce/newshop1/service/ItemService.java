package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.repository.ItemRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ItemService {

    // private final ItemRepository itemRepository;
    private final ItemRepositoryImpl itemRepository;


    @Transactional(readOnly = true)
    public Page<ItemDto> searchAll(SearchDto searchDto, Pageable pageable){
        return itemRepository.searchAll(searchDto, pageable);
    }

}
