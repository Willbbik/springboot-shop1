package com.ecommerce.newshop1.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ExtendWith(SpringExtension.class)
//@WebMvcTest(MemberController.class)
//@DisplayName("MemberController 테스트")
public class MemberControllerTest extends AbstractControllerTest{

    @Autowired
    private MemberController memberController;

    @Override
    protected Object controller() {
        return memberController;
    }

    @Test
    public void MainTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"));
    }


}
