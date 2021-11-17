package com.ecommerce.newshop1.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    // 테스트 실패함

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "qweqwe", roles = {"MEMBER"})
    public void categoryAllPage() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/category/all"))
                .andExpect(status().isOk());
    }

}