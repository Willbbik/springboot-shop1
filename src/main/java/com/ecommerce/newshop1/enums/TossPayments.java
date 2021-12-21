package com.ecommerce.newshop1.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TossPayments {


    DONE("DONE"),
    CANCELED("CANCELED"),
    PARTIAL_CANCELED("PARTIAL_CANCELED");

    private String value;

}
