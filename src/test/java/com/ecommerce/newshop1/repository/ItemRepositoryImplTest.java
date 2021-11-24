package com.ecommerce.newshop1.repository;


import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.repository.custom.ItemRepositoryImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRepositoryImplTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemRepositoryImpl itemRepositoryImpl;

    @Test
    public void nooffset_첫페이지() throws Exception{

        //given
        String a = "a";



        for(int i = 1; i <= 30; i++){
            itemRepository.save(Item.builder()
                    .itemName(a+i)
                    .price(30000)
                    .imageUrl("url")
                    .saleStatus("onsale")
                    .build());
        }

        //when
        List<ItemDto> items = itemRepositoryImpl.searchAllNoOffset(null);

        //then
        assertThat(items).hasSize(10);
        assertThat(items.get(0).getItemName()).isEqualTo("a30");
        assertThat(items.get(9).getItemName()).isEqualTo("a21");

    }


}
