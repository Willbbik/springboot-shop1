package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.ItemImageDto;
import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.ItemImage;
import com.ecommerce.newshop1.exception.ItemNotFoundException;
import com.ecommerce.newshop1.repository.ItemImageRepository;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final RedisService redisService;

    ModelMapper mapper = new ModelMapper();

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable){

        return itemRepository.searchAll(searchDto, pageable);
    }

    @Override
    public List<ItemImageDto> searchAllItemImage(Item itemId) {
        return itemImageRepository.searchAll(itemId);
    }


    @Override
    @Transactional(readOnly = true)
    public Long searchTotal(SearchDto searchDto) {

        return itemRepository.searchTotal(searchDto);
    }


    @Override
    public List<ItemDto> searchAllNoOffset(Long ItemId) {

        return itemRepository.searchAllNoOffset(ItemId);
    }

    @Override
    public void saveItem(Item item) {
        itemRepository.save(item);
    }


    @Override
    public void saveItemImage(ItemImage itemImage) {
        itemImageRepository.save(itemImage);
    }


    @Override
    public String createOrderId(String nowDate, int totalPrice) throws Exception {
        return redisService.createOrderId(nowDate, totalPrice);
    }

    @Override
    @Transactional
    public List<ItemDto> itemToPayment(String itemList) {

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = (JsonArray) jsonParser.parse(itemList);
        JsonObject jsonItem = (JsonObject) jsonElements.get(0);

        Long itemId = Long.parseLong(jsonItem.get("itemId").getAsString());
        int quantity = Integer.parseInt(jsonItem.get("quantity").getAsString());

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("해당 상품이 존재하지 않습니다. 상품번호 : " + itemId));

        ItemDto itemDto = mapper.map(item, ItemDto.class);
        itemDto.setTotalPrice(item.getPrice() * quantity);
        itemDto.setQuantity(quantity);

        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);

        return itemDtoList;
    }


}
