package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Order;
import com.ecommerce.newshop1.utils.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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
