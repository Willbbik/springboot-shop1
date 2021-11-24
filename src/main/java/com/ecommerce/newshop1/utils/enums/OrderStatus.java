package com.ecommerce.newshop1.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    PAY_WAITING("입금대기"),
    PAY_COMPLETION("입금완료"),
    DEPOSIT_READY("배송준비중"),
    DEPOSIT_COMPLETION("배송완료"),
    ORDER_CANCEL("주문취소");

    private String value;

}
