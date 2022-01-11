package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.ItemImageDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.ItemImage;
import com.ecommerce.newshop1.exception.ItemNotFoundException;
import com.ecommerce.newshop1.repository.ItemImageRepository;
import com.ecommerce.newshop1.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final AwsS3Service awsS3Service;

    @Override
    public Long getLastId(List<ItemDto> itemList, Long lastId) {

        if(itemList.isEmpty()){
            return lastId;
        }else{
            return itemList.stream()
                    .min(Comparator.comparingLong(ItemDto::getId))
                    .get().getId();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Item findById(Long itemId) {

        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("해당 상품이 존재하지 않습니다. itemId : " + itemId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable){

        return itemRepository.searchAll(searchDto, pageable);
    }

    @Override
    @Transactional
    public List<ItemImageDto> searchAllItemImage(Item itemId) {

        List<ItemImageDto> imageDtos = itemImageRepository.searchAll(itemId);

        for(ItemImageDto itemImageDto : imageDtos){
            itemImageDto.setImageUrl(awsS3Service.getS3FileUrl(itemImageDto.getImageUrl()));
        }
        return imageDtos;
    }


    @Override
    @Transactional(readOnly = true)
    public Long searchTotal(SearchDto searchDto) {

        return itemRepository.searchTotal(searchDto);
    }


    @Override
    @Transactional
    public List<ItemDto> searchAllNoOffset(String category, Long ItemId) {

        return itemRepository.searchAllNoOffset(category, ItemId);
    }

    @Override
    @Transactional
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }


    @Override
    @Transactional
    public ItemImage saveItemImage(ItemImage itemImage) {
        return itemImageRepository.save(itemImage);
    }



}
