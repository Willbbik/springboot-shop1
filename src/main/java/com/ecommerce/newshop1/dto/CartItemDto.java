package com.ecommerce.newshop1.dto;


import com.ecommerce.newshop1.entity.Cart;
import com.ecommerce.newshop1.entity.Item;
import lombok.*;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

    private Long id;
    private Cart cart;
    private Item item;
    private int quantity;

}
