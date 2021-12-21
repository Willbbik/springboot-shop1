package com.ecommerce.newshop1.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PayType {

    VIRTUAL_ACCOUNT("가상계좌"),
    KAKAO_PAY("카카오페이"),
    CARD("신용카드"),
    EMPTY("없음");

    private String title;

    public static PayType findByPayType(String payType){
        return Arrays.stream(PayType.values())
                .filter(payment -> payment.getTitle().equals(payType))
                .findAny()
                .orElse(EMPTY);
    }


}
