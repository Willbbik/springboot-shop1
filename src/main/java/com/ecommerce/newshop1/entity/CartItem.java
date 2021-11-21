package com.ecommerce.newshop1.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "item_id")
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
