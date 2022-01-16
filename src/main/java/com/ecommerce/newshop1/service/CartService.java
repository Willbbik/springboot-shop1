package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.CartItemDto;
import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.entity.Member;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface CartService {

    void createCart(Member member);

    void addCart(Long id, int quantity);

    void deleteCartItem(Long id);

    void deleteCartItemAll(List<Long> id);

    void updateQuantity(Long id, int quantity);

    List<CartItemDto> findCartItems(Member member);

    List<OrderItemDto> cartItemToPayment(String cartIdList, HttpSession session);

}
