package com.ecommerce.newshop1.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery_address")
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_address_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
    private String customerName;
    private String customerPhoneNum;
    private String recipientName;
    private String recipientPhoneNum;
    private String zipcode;
    private String address;
    private String detailAddress;



}
