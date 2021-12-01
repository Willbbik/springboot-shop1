package com.ecommerce.newshop1.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public enum PayMethod {

    CASH("현금", Arrays.asList(PayType.VIRTUAL_ACCOUNT)),
    CARD("카드", Arrays.asList(PayType.KAKAO_PAY)),
    EMPTY("없음", Collections.EMPTY_LIST);

    private String title;
    private List<PayType> payList;

    public static PayMethod findByPayType(String payType){
        return Arrays.stream(PayMethod.values())
                .filter(payment -> payment.hasPayType(payType))
                .findAny()
                .orElse(EMPTY);
    }

    public boolean hasPayType(String payType){
        return payList.stream()
                .anyMatch(pay -> pay.getValue().equals(payType));
    }

    //    public static Payment findByPayCode(PayType payType){
//        return Arrays.stream(Payment.values())
//                .filter(payment -> payment.hasPayCode(payType))
//                .findAny()
//                .orElse(EMPTY);
//    }
//
//    public boolean hasPayCode(PayType payType){
//        return payList.stream()
//                .anyMatch(pay -> pay == payType);
//    }

}
