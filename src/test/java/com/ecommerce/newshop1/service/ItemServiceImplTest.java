package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.repository.ItemRepositoryImpl;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest(classes = ItemServiceImpl.class)
@Transactional
public class ItemServiceImplTest {

    @Autowired
    private ItemRepositoryImpl itemRepository;

    @Test
    public void searchAll(){

        SearchDto searchDto = new SearchDto();
        searchDto.setSaleStatus("onsale");
        searchDto.setCategory("top");
        searchDto.setItemName("captest");

        PageRequest pageRequest = PageRequest.of(0, 1, Sort.Direction.DESC, "id");

        List<ItemDto> result = itemRepository.searchAll(searchDto, pageRequest);

        result.forEach(
                x -> System.out.println("element : " + x)
        );
    }

    @Test
    public void isBlankTest(){

        String s = "";

        if(StringUtils.isBlank(s)){
            System.out.println("no");
        }else{
            System.out.println("good");
        }

    }


}

