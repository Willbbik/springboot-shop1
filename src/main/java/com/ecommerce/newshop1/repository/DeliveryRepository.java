package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Delivery;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Long countByDeliveryStatus(DeliveryStatus deliveryStatus);



}
