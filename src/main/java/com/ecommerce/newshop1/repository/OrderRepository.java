package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
