package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.ItemImageDto;
import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.ItemImage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {

    Item findById(Long id);

    List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable);

    Long searchTotal(SearchDto searchDto);

    List<ItemDto> searchAllNoOffset(Long ItemId);

    List<ItemImageDto> searchAllItemImage(Item item);

    Item saveItem(Item item);

    ItemImage saveItemImage(ItemImage itemImage);

}

