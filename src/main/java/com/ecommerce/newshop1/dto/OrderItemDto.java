package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Order;
import com.ecommerce.newshop1.utils.enums.DeliveryStatus;
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

    private int quantity;

    private int totalPrice;

}
