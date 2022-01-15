package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.Order;
import com.ecommerce.newshop1.repository.custom.OrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    List<Order> findAllByMember(Member member);

    Optional<Order> findByOrderNum(String orderNum);

    boolean existsByOrderNum(String orderNum);

}
