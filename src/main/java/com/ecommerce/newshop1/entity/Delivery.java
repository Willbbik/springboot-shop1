package com.ecommerce.newshop1.entity;


import com.ecommerce.newshop1.utils.enums.DeliveryStatus;
import com.ecommerce.newshop1.utils.enums.DepositStatus;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @OneToOne
    @JoinColumn(name = "orders")
    private Order order;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_address")
    private DeliveryAddress deliveryAddress;

    @Enumerated(EnumType.STRING)
    private DepositStatus depositStatus;

    @ColumnDefault("0")
    private int totalPrice;

    @Column(name = "order_num", nullable = false)
    private String orderNum;

}
