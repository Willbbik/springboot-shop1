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
    @JoinColumn(name = "order")
    private Order order;

    @OneToOne
    @JoinColumn(name = "member_address")
    private MemberAddress memberAddress;

    @Enumerated(EnumType.STRING)
    private DepositStatus depositStatus;

    @ColumnDefault("0")
    private int totalPrice;

    @Column(name = "order_num", nullable = false)
    private String orderNum;

}
