package com.ecommerce.newshop1.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class Cart extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "cart")
    private List<CartItem> cartItemList = new ArrayList<CartItem>();

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void createCart(Member member){
        this.member = member;
        member.setCart(this);
    }


}
