package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.CartDto;
import com.ecommerce.newshop1.dto.CartItemDto;
import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.entity.*;
import com.ecommerce.newshop1.exception.CartNotFoundException;
import com.ecommerce.newshop1.exception.ItemNotFoundException;
import com.ecommerce.newshop1.repository.CartItemRepository;
import com.ecommerce.newshop1.repository.CartRepository;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.dto.CartQuantityUpdateDto;
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
public class CartService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final SecurityService security;

    ModelMapper mapper = new ModelMapper();


    public void createCart(Member member){
        Cart cart = new Cart();
        cart.createCart(member);
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
    public List<OrderItemDto> cartItemToPayment(String cartIdList, HttpSession session){

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = (JsonArray) jsonParser.parse(cartIdList);

        List<Long> cartItemIdList = new ArrayList<>();
        List<OrderItemDto> itemDtoList = new ArrayList<>();

        // 상품 번호와 개수를 배열에 차례대로 담기
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
                    .totalPrice(cartItem.getItem().getPrice() * cartItem.getQuantity())
                    .deliveryStatus(DeliveryStatus.DEPOSIT_READY)
                    .build();
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }

    // 장바구니 상품 하나만 삭제
    @Transactional
    public void deleteCartItemById(Long id){
        cartItemRepository.deleteById(id);
    }

    // 장바구니 상품 여러개 삭제
    @Transactional
    public void deleteCartItemAllById(List<Long> id){
        cartItemRepository.deleteAllById(id);
    }

    // 장바구니 상품 수량 변경
    @Transactional
    public void updateQuantity(CartQuantityUpdateDto updateDto){
        CartItem cartItem = cartItemRepository.findById(updateDto.getId())
                .orElseThrow(() -> new CartNotFoundException("존재하지 않는 장바구니 상품입니다."));
        cartItem.setQuantity(updateDto.getQuantity());
    }

}
