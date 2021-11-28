package com.ecommerce.newshop1.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PayType {

    VIRTUAL_ACCOUNT("가상계좌"),
    KAKAO_PAY("카카오페이"),
    NAVER_PAY("네이버페이"),
    EMPTY("없음");

    private String value;

    public static PayType findWe(String payment){

        return Arrays.stream(PayType.values())
                .filter(paytype -> paytype.getValue().equals(payment))
                .findAny()
                .orElse(EMPTY);
    }

}
