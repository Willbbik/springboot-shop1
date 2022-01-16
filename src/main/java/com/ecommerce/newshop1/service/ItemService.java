package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.ItemImageDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.entity.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;

public interface ItemService {

    String getLastId(List<ItemDto> itemList, String sort, String value);

    Item findById(Long id);

    Item saveItem(MultipartHttpServletRequest mtfRequest, ItemDto itemDto) throws IOException;

    Long getLastId(List<ItemDto> itemList, Long lastId);

    Long searchTotal(SearchDto searchDto);

    List<ItemDto> searchAllBySort(String itemName, String sort, String value, Pageable pageable);

    List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable);

    List<ItemDto> searchAllNoOffset(String category, Long ItemId);

    List<ItemImageDto> searchAllItemImage(Item item);

}

