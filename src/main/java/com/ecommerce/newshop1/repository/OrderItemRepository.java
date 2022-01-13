package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.OrderItem;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import com.ecommerce.newshop1.repository.custom.OrderItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> , OrderItemRepositoryCustom {

    Long countByDeliveryStatus(DeliveryStatus deliveryStatus);

}
