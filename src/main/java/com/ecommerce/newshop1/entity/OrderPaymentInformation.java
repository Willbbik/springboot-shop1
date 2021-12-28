package com.ecommerce.newshop1.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "order_payment_info")
public class OrderPaymentInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_payment_info_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private String payType;
    private String bank;
    private String accountNumber;
    private String dueDate;
    private String company;
    private String number;

}

