package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.CartItemDto;
import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.entity.*;
import com.ecommerce.newshop1.exception.CartNotFoundException;
import com.ecommerce.newshop1.exception.ItemNotFoundException;
import com.ecommerce.newshop1.exception.MemberNotFoundException;
import com.ecommerce.newshop1.repository.CartItemRepository;
import com.ecommerce.newshop1.repository.CartRepository;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final SecurityService security;

    ModelMapper mapper = new ModelMapper();

    @Override
    public void createCart(Member member){
        Cart cart = new Cart();
        cart.createCart(member);
    }

    @Override
    @Transactional
    public void addCart(Long id, int quantity){

        // 사용자 아이디 가져오기
        String userId = security.getName();

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 유저입니다."));
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("해당 상품을 찾을 수 없습니다. 상품 번호 : " + id));
        Cart cart = cartRepository.findByMember(member)
                .orElseThrow(() -> new CartNotFoundException("존재하지 않는 장바구니입니다."));
        CartItem cartItem = cartItemRepository.findByCartAndItem(cart, item);

        // 장바구니에 해당 상품이 존재하지 않는다면 생성
        if(cartItem == null){
            cartItem = CartItem.createCartItem(cart, item, quantity);
        }else{
            cartItem.addQuantity(quantity);
        }

        cartItem.setTotalPrice(cartItem.getItem().getPrice() * cartItem.getQuantity());
        cartItemRepository.save(cartItem);

    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemDto> findCartItems(Member member){

        Cart cart = cartRepository.findByMember(member)
                         .orElseThrow(() -> new CartNotFoundException("존재하지 않는 장바구니입니다."));

        // 장바구니의 상품들 가져오기
        List<CartItemDto> cartItemList = cartItemRepository.findByCartId(cart.getId()).stream()
                        .map(p -> mapper.map(p, CartItemDto.class))
                        .collect(Collectors.toList());

        return cartItemList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDto> cartItemToPayment(String cartIdList, HttpSession session){

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = (JsonArray) jsonParser.parse(cartIdList);

        List<Long> cartItemIdList = new ArrayList<>();
        List<OrderItemDto> itemDtoList = new ArrayList<>();


        for (int i = 0; i <jsonElements.size(); i++) {

            JsonObject jsonObject = (JsonObject) jsonElements.get(i);
            Long cartItemId = jsonObject.get("cartItemId").getAsLong();

            cartItemIdList.add(cartItemId);
        }
        session.setAttribute("cartItemIdList", cartItemIdList);

        for(int i = 0; i < cartItemIdList.size(); i++){
            CartItem cartItem = cartItemRepository.findById((cartItemIdList.get(i)))
                    .orElseThrow(() -> new ItemNotFoundException("해당 장바구니 상품이 존재하지 않습니다."));

            OrderItemDto itemDto = OrderItemDto.builder()
                    .item(cartItem.getItem())
                    .quantity(cartItem.getQuantity())
                    .totalPrice(cartItem.getTotalPrice())
                    .deliveryStatus(DeliveryStatus.DEPOSIT_READY)
                    .build();
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }

    @Override
    @Transactional
    public void deleteCartItem(Long id){

        cartItemRepository.deleteById(id);
    }


    @Override
    @Transactional
    public void deleteCartItemAll(List<Long> id){

        cartItemRepository.deleteAllById(id);
    }


    @Override
    @Transactional
    public void updateQuantity(Long id, int quantity){
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("존재하지 않는 장바구니 상품입니다."));

        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(cartItem.getItem().getPrice() * quantity);
    }

}
