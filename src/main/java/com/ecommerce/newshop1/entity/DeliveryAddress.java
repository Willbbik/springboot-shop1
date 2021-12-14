package com.ecommerce.newshop1.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_address_id")
    private Long id;

    @OneToOne
    private Delivery delivery;

    private String customerName;
    private String customerPhoneNum;
    private String recipientName;
    private String recipientPhoneNum;
    private String zipcode;
    private String address;
    private String detailAddress;



}
