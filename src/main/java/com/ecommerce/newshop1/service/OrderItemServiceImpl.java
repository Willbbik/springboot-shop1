package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.entity.OrderItem;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import com.ecommerce.newshop1.exception.OrderNotFoundException;
import com.ecommerce.newshop1.repository.OrderItemRepository;
import com.ecommerce.newshop1.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService{

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

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


    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDto> searchByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable) {
        List<OrderItemDto> orderItemDtos = orderRepository.searchItemDtoByDeliveryStatus(deliveryStatus, pageable);
        for(OrderItemDto dto : orderItemDtos){
            dto.setDeliveryAddress(dto.getOrder().getDelivery().getDeliveryAddress());
        }
        return orderItemDtos;
    }

}
