package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.CartDto;
import com.ecommerce.newshop1.dto.CartItemDto;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.exception.MemberNotFoundException;
import com.ecommerce.newshop1.repository.CartRepository;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.service.CartService;
import com.ecommerce.newshop1.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final SecurityService security;

    @GetMapping("/cart")
    public String cart(Model model){

        Member member = memberRepository.findByuserId(security.getName())
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다."));

        List<CartItemDto> cartItems = cartService.findCartItems(member);
        model.addAttribute("cartItems", cartItems);

        return "cart";
    }


    @PostMapping("/cart/add")
    @ResponseBody
    public int cartAdd(@Valid CartDto cartDto, BindingResult bindingResult){

        if(!security.isAuthenticated()){        // 권한 체크
            return 403;
        } else if(bindingResult.hasErrors()){   // cartItemDto객체 유효성 검사
            return 401;
        } else{
            cartService.addCart(cartDto);
            return 200;
        }
    }



}
