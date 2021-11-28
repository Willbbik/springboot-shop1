package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.dto.CartItemDto;
import com.ecommerce.newshop1.entity.Cart;
import com.ecommerce.newshop1.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    List<CartItem> findByCartId(Long cartId);


}
