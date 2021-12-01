//package com.ecommerce.newshop1.controller;
//
//import com.ecommerce.newshop1.repository.ItemRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(OrderController.class)
//@DisplayName("OrderController 테스트")
//public class OrderControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ItemRepository itemRepository;
//
////    @BeforeEach
////    public void before(){
////            mockMvc =
////                    MockMvcBuilders
////                    .standaloneSetup(OrderController.class)
////                    .addFilter(new CharacterEncodingFilter("UTF-8", true))
////                    .build();
////    }
//
//    @Test
//    public void payMethodCheck() throws Exception {
//
//        mockMvc.perform(
//                MockMvcRequestBuilders
//                .post("/order/paymethod/check")
//                .content("VIRTUAL_ACCOUNT"))
//                .andExpect(status().isCreated());
//        }
//
//}
