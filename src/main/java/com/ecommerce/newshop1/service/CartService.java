package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.CartDto;
import com.ecommerce.newshop1.dto.CartItemDto;
import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.entity.*;
import com.ecommerce.newshop1.exception.ItemNotFoundException;
import com.ecommerce.newshop1.repository.CartItemRepository;
import com.ecommerce.newshop1.repository.CartRepository;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final SecurityService security;

    ModelMapper mapper = new ModelMapper();

    @Transactional
    public void createCart(Member member){

        Cart cart = Cart.createCart(member);
        cartRepository.save(cart);
    }

    @Transactional
    public void addCart(CartDto cartDto){

        // 사용자 아이디 가져오기
        String userId = security.getName();

        // cartItem에 저장하기 위해서
        Member member = memberRepository.findByuserId(userId).get();
        Item item = itemRepository.findById(cartDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("해당 상품을 찾을 수 없습니다. 상품 번호 : " + cartDto.getItemId()));

        Cart cart = cartRepository.findByMemberId(member.getId());
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        // 장바구니에 해당 상품이 존재하지 않는다면 생성
        if(cartItem == null){
            cartItem = CartItem.createCartItem(cart, item, cartDto.getQuantity());
        }else{
            cartItem.addQuantity(cartDto.getQuantity());
        }
        cartItemRepository.save(cartItem);

    }

    @Transactional(readOnly = true)
    public List<CartItemDto> findCartItems(Member member){

        // 사용자의 장바구니 찾기
        Cart cart = cartRepository.findByMemberId(member.getId());

        // 장바구니의 상품들 가져오기
        List<CartItem> cartItemList = cartItemRepository.findByCartId(cart.getId());

        return cartItemList.stream()
                .map(p -> mapper.map(p, CartItemDto.class))
                .peek(p -> p.setTotalPrice(p.getItem().getPrice() * p.getQuantity()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderItemDto> cartItemToPayment(String itemList){

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = (JsonArray) jsonParser.parse(itemList);

        List<Long> itemIdList = new ArrayList<>();
        List<Integer> quantityList = new ArrayList<>();
        List<OrderItemDto> itemDtoList = new ArrayList<>();

        // 상품 번호와 개수를 배열에 차례대로 담기
        for (int i = 0; i <jsonElements.size(); i++) {

            JsonObject jsonObject = (JsonObject) jsonElements.get(i);
            Long itemId = Long.parseLong(jsonObject.get("itemId").getAsString());
            int quantity = Integer.parseInt(jsonObject.get("quantity").getAsString());

            itemIdList.add(itemId);
            quantityList.add(quantity);
        }

        for(int i = 0; i < itemIdList.size(); i++){
            Item item = itemRepository.findById(itemIdList.get(i))
                    .orElseThrow(() -> new ItemNotFoundException("해당 상품이 존재하지 않습니다."));

            OrderItemDto itemDto = OrderItemDto.builder()
                    .item(item)
                    .quantity(quantityList.get(i))
                    .totalPrice(item.getPrice() * quantityList.get(i))
                    .build();

            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }




}
