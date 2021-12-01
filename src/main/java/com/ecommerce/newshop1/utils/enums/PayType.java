package com.ecommerce.newshop1.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PayType {

    VIRTUAL_ACCOUNT("가상계좌", "VIRTUAL_ACCOUNT"),
    KAKAO_PAY("카카오페이", "KAKAO_PAY"),
    CARD("신용/체크카드", "CARD"),
    EMPTY("없음", "없음");

    private String title;
    private String value;

}
