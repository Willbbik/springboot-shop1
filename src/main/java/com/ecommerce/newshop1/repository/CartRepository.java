package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Cart;
import com.ecommerce.newshop1.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByMember(Member member);

}
