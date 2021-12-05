package com.ecommerce.newshop1.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {

    DEPOSIT_READY("입금대기"),
    DELIVERY_READY("배송준비중"),
    DELIVERY_START("배송중"),
    DELIVERY_COMPLETION("배송완료"),
    RETURN_REQUEST("반품요청중"),
    RETURN_START("반품진행중"),
    RETURN_SUCCESS("반품완료"),
    ORDER_CANCEL("주문취소");

    private String value;
}
