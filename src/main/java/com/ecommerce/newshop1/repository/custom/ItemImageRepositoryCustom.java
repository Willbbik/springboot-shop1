package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.ItemImageDto;
import com.ecommerce.newshop1.entity.Item;

import java.util.List;

public interface ItemImageRepositoryCustom {

    List<ItemImageDto> searchAll(Item itemId);


}
