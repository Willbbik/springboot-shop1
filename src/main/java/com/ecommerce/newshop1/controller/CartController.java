package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.CartItemDto;
import com.ecommerce.newshop1.repository.CartRepository;
import com.ecommerce.newshop1.service.CartService;
import com.ecommerce.newshop1.utils.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final SecurityService security;


    @PostMapping("/cart/add")
    @ResponseBody
    public int cartAdd(@Valid CartItemDto cartItemDto, BindingResult bindingResult){

        // 권한 체크
        if(!security.isAuthenticated()){
            return 403;
        }

        // cartItemDto객체 유효성 검사
        if(bindingResult.hasErrors()) return 401;

        cartService.addCart(cartItemDto);
        return 200;
    }


}
