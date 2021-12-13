package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.OrderDto;
import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.Order;

import java.util.List;

public interface OrderRepositoryCustom {

    List<OrderDto> searchAllByMember(Long orderId, Member member);

}
