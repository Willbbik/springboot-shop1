package com.ecommerce.newshop1.service;


import com.ecommerce.newshop1.dto.ItemDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    ItemService itemService;


    @Test
    void searchAllBySort(){

        //given

        Pageable pageable = PageRequest.ofSize(9);
        List<ItemDto> itemList = itemService.searchAllBySort("비니", "lowPrice", null, pageable);

        Assertions.assertTrue(!itemList.isEmpty());
    }


}
