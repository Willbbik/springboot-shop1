package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
