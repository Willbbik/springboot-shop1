package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.entity.OrderItem;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import com.ecommerce.newshop1.exception.OrderNotFoundException;
import com.ecommerce.newshop1.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService{

    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional(readOnly = true)
    public Long countByDeliveryStatus(DeliveryStatus deliveryStatus) {
        return orderItemRepository.countByDeliveryStatus(deliveryStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItem findById(Long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("존재하지 않는 주문상품입니다. orderItemId : " + id));
    }

    @Override
    @Transactional
    public void save(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }
}
