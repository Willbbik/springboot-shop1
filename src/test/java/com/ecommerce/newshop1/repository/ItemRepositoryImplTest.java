package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemRepositoryImplTest {

    @Autowired
    private ItemService itemService;

    @Test
    public void searchAll(){
        SearchDto searchDto = new SearchDto();
        searchDto.setCategory("top");

        PageRequest pageRequest = PageRequest.of(0, 1, Sort.Direction.DESC, "id");

        Page<ItemDto> result = itemService.searchAll(searchDto, pageRequest);

        result.forEach(
                x -> System.out.println("element : " + x)
        );
    }


}
