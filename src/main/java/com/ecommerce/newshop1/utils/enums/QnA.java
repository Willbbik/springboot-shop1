package com.ecommerce.newshop1.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QnA {


    EMPTY("empty"),
    PRESENT("present"),
    PRIVATE("private"),
    PUBLIC("public");

    private final String value;


}
