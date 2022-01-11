package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.entity.OrderItem;
import com.ecommerce.newshop1.enums.DeliveryStatus;

public interface OrderItemService {

    Long countByDeliveryStatus(DeliveryStatus deliveryStatus);

    OrderItem findById(Long id);

    void save(OrderItem orderItem);

}
