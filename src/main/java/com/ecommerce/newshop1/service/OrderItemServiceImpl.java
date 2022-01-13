package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.entity.Order;
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
    public Long searchTotal(DeliveryStatus deliveryStatus, SearchDto searchDto) {

        return orderItemRepository.searchTotal(deliveryStatus, searchDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItem findById(Long id) {

        return orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("존재하지 않는 주문상품입니다. orderItemId : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsOrderItem(String orderNum, Long orderItemId) {

        Order order = orderRepository.findByOrderNum(orderNum)
                .orElseThrow(() -> new OrderNotFoundException("존재하지 않는 주문번호입니다."));
        return order.getOrderItems().stream()
                .anyMatch(p -> p.getId().equals(orderItemId));
    }

    @Override
    @Transactional
    public void save(OrderItem orderItem) {

        orderItemRepository.save(orderItem);
    }

    @Override
    @Transactional
    public void saveWayBillNum(Long orderItemId , String wayBillNum) {

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderNotFoundException("존재하지 않는 주문입니다."));

        orderItem.setWayBillNum(wayBillNum);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDto> searchByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable) {

        List<OrderItemDto> orderItemList = orderItemRepository.searchByDeliveryStatus(deliveryStatus, pageable);

//        for(OrderItemDto dto : orderItemList){
//            dto.setDeliveryAddress(dto.getOrder().getDelivery().getDeliveryAddress());
//        }
        return orderItemList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDto> searchAllByDeliveryStatusAndSearchDto(DeliveryStatus deliveryStatus, SearchDto searchDto, Pageable pageable) {

        return orderItemRepository.searchAllByDeliveryStatusAndSearchDto(deliveryStatus, searchDto, pageable);
    }


}
