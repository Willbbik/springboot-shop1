package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {


    private Long id;

    private Order order;

    private Item item;

    private Long itemId;

    private int quantity;

    private int totalPrice;



}
