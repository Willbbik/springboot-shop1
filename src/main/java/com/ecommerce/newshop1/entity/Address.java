package com.ecommerce.newshop1.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Address {

    private String zipcode;
    private String address;
    private String detailAddress;

    public Address(String zipcode, String address, String detailAddress) {
        this.zipcode = zipcode;
        this.address = address;
        this.detailAddress = detailAddress;
    }

}

