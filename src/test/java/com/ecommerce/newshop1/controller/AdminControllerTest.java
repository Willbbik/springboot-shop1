package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.ItemOptDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void testSaveItem(){

        // given
        Item item = Item.builder()
                .category("top")
                .model("173/67")
                .price(20000)
                .productInfo("very good")
                .productName("후드티")
                .build();

        ItemOptDto itemOptDto = ItemOptDto.builder()
                .item(item)
                .color("블랙")
                .size("L")
                .quantity(2)
                .build();

        // when





    }

}
