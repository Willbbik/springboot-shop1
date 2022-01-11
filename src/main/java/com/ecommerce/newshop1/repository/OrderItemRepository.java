package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.OrderItem;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Long countByDeliveryStatus(DeliveryStatus deliveryStatus);

}
