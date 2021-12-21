package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.OrderDto;
import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.Order;
import com.ecommerce.newshop1.enums.DeliveryStatus;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> searchAllByMember(Long orderId, Member member);

    List<OrderItemDto> searchByDeliveryStatus(DeliveryStatus deliveryStatus);


}

