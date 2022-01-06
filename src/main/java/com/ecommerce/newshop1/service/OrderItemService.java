package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.entity.OrderItem;

public interface OrderItemService {

    OrderItem findById(Long id);

    void save(OrderItem orderItem);

}
