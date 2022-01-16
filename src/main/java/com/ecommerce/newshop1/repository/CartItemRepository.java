package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Cart;
import com.ecommerce.newshop1.entity.CartItem;
import com.ecommerce.newshop1.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartAndItem(Cart cart, Item item);

    List<CartItem> findByCartId(Long cartId);


}
