package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.entity.Cart;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;


    @Transactional
    public void createCart(Member member){

        Cart cart = Cart.createCart(member);
        cartRepository.save(cart);
    }



}
