package com.ecommerce.newshop1.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayType {

    REMITTANCE("무통장입금"),
    ACCOUNT_TRANSFER("계좌이체"),
    KAKAO_PAY("카카오페이"),
    NAVER_PAY("네이버페이");

    private String title;

}
