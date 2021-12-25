package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.CartDto;
import com.ecommerce.newshop1.dto.CartItemDto;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.exception.MemberNotFoundException;
import com.ecommerce.newshop1.repository.CartRepository;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.service.CartService;
import com.ecommerce.newshop1.service.SecurityService;
import com.ecommerce.newshop1.dto.CartQuantityUpdateDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
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
    @ApiOperation(value = "장바구니에 상품 추가")
    public @ResponseBody int cartAdd(@Valid CartDto cartDto, BindingResult bindingResult){

        if(!security.isAuthenticated()){        // 권한 체크
            return 403;
        } else if(bindingResult.hasErrors()){   // cartItemDto객체 유효성 검사
            return 401;
        } else{
            cartService.addCart(cartDto);
            return 200;
        }
    }


    @PatchMapping("/cart/update/quantity")
    @ApiOperation(value = "장바구니 상품 수량 변경")
    public @ResponseBody String updateQuantity(@Validated CartQuantityUpdateDto updateDto){
        cartService.updateQuantity(updateDto);
        return "수량 변경 완료";
    }


    @DeleteMapping("/cart/delete/item")
    public @ResponseBody String deleteCartItem(Long id){
        cartService.deleteCartItemById(id);
        return "장바구니 상품 삭제 완료";
    }

    @DeleteMapping("/cart/delete/itemList")
    public @ResponseBody String deleteCartItemAll(@RequestParam List<Long> itemList){
        cartService.deleteCartItemAllById(itemList);
        return "장바구니 상품 삭제 완료";
    }


}
