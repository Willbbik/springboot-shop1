package com.ecommerce.newshop1.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {

    DEPOSIT_READY("입금대기"),
    DEPOSIT_PRICE_LACK("입금금액부족"),
    DEPOSIT_SUCCESS("입금완료"),
    DELIVERY_READY("배송준비중"),
    DELIVERY_ING("배송중"),
    DELIVERY_COMPLETION("배송완료"),
    ORDER_CANCEL("주문취소");

    private String value;
}
