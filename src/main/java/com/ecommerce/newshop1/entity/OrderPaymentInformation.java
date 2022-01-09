package com.ecommerce.newshop1.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
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

    @Column
    private String payType;

    @Column
    private String bank;

    @Column
    private String customerName;

    @Column
    private String accountNumber;

    @Column
    private String dueDate;

    @Column
    private String company;

    @Column
    private String number;

}

