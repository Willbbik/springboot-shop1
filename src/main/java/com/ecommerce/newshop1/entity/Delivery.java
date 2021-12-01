package com.ecommerce.newshop1.entity;


import com.ecommerce.newshop1.utils.enums.DeliveryStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    @OneToOne
    private Order order;

    @OneToOne
    private MemberAddress memberAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private int totalPrice;

}
