package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Order;
import com.ecommerce.newshop1.repository.custom.OrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    Order findByOrderNum(String orderNum);

    boolean existsByOrderNum(String orderNum);

}
