package com.ecommerce.newshop1.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ColumnDefault("0")
    private int quantity;

    public static CartItem createCartItem(Cart cart, Item item, int quantity){

        CartItem cartItem = new CartItem();
        cartItem.cart = cart;
        cartItem.item = item;
        cartItem.quantity = quantity;
        return cartItem;
    }

    public void addQuantity(int quantity){
        this.quantity += quantity;
    }

    public void updateQuantity(int quantity){
        this.quantity = quantity;
    }

}
