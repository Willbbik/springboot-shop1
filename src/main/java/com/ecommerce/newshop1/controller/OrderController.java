package com.ecommerce.newshop1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {


    @GetMapping("/order/checkout")
    public String checkout(){
        return "order/order_checkout";
    }

}
