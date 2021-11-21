package com.ecommerce.newshop1.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "cart")
public class Cart extends TimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static Cart createCart(Member member){
        Cart cart = new Cart();
        cart.member = member;
        return cart;
    }

}
