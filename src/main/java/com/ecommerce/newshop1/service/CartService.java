package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.CartItemDto;
import com.ecommerce.newshop1.entity.Cart;
import com.ecommerce.newshop1.entity.CartItem;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.exception.ItemNotFoundException;
import com.ecommerce.newshop1.repository.CartItemRepository;
import com.ecommerce.newshop1.repository.CartRepository;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.utils.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final SecurityService security;

    @Transactional
    public void createCart(Member member){

        Cart cart = Cart.createCart(member);
        cartRepository.save(cart);
    }

    @Transactional
    public void addCart(CartItemDto cartItemDto){

        // 사용자 아이디 가져오기
        String userId = security.getName();

        // cartItem에 저장하기 위해서
        Member member = memberRepository.findByuserId(userId).get();
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("해당 상품을 찾을 수 없습니다. 상품 번호 : " + cartItemDto.getItemId()));

        Cart cart = cartRepository.findByMemberId(member.getId());
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        // 장바구니에 해당 상품이 존재하지 않는다면 생성
        if(cartItem == null){
            cartItem = CartItem.createCartItem(cart, item, cartItemDto.getQuantity());
        }else{
            cartItem.addQuantity(cartItemDto.getQuantity());
        }
        cartItemRepository.save(cartItem);

    }



}
