package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.DeliveryAddress;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Order;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private Long id;

    private Order order;

    private Item item;

    private DeliveryStatus deliveryStatus;

    private String wayBillNum;

    private int quantity;

    private int totalPrice;

    private DeliveryAddress deliveryAddress;


}
