package com.ecommerce.newshop1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {

    private String saleStatus;
    private String category;
    private String itemName;

    private String deliveryStatus;
    private String customerName;
    private String deliveryId;
    private String orderNum;
    private String keyType;
    private String keyValue;

}
