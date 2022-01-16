package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.CartDto;
import com.ecommerce.newshop1.dto.CartItemDto;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.service.CartService;
import com.ecommerce.newshop1.service.MemberService;
import com.ecommerce.newshop1.utils.CommonService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final MemberService memberService;
    private final CartService cartService;
    private final CommonService commonService;

    @GetMapping("/cart")
    public String cart(Model model){

        Member member = memberService.getCurrentMember();

        List<CartItemDto> cartItems = cartService.findCartItems(member);
        model.addAttribute("cartItems", cartItems);

        return "cart";
    }


    @PostMapping("/cart/add")
    @ApiOperation(value = "장바구니에 상품 추가")
    public @ResponseBody String cartAdd(@Validated CartDto cartDto, BindingResult errors){

        if(errors.hasErrors()) return commonService.getErrorMessage(errors);

        cartService.addCart(cartDto.getId(), cartDto.getQuantity());
        return "success";
    }


    @PatchMapping("/cart/update/quantity")
    @ApiOperation(value = "장바구니 상품 수량 변경")
    public @ResponseBody String updateQuantity(@Validated CartDto cartDto, BindingResult errors){

        if(errors.hasErrors()) return commonService.getErrorMessage(errors);

        cartService.updateQuantity(cartDto.getId(), cartDto.getQuantity());
        return "success";
    }


    @DeleteMapping("/cart/delete/item")
    @ApiOperation(value = "장바구니 단일 상품 삭제")
    public @ResponseBody String deleteCartItem(@RequestParam(name = "id") Long id){

        cartService.deleteCartItem(id);
        return "success";
    }


    @DeleteMapping("/cart/delete/itemList")
    @ApiOperation(value = "장바구니 상품 삭제")
    public @ResponseBody String deleteCartItemAll(@RequestParam(required = false) List<Long> itemList){

        cartService.deleteCartItemAll(itemList);
        return "success";
    }


}
