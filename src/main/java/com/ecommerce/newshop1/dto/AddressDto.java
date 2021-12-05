package com.ecommerce.newshop1.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    private String customerName;
    private String customerPhoneNum;
    private String phoneNum;
    private String zipcode;
    private String address;
    private String detailAddress;



}
