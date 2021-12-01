package com.ecommerce.newshop1.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DepositStatus {

    DEPOSIT_READY("입금대기"),
    DEPOSIT_SUCCESS("입금완료"),
    REFUND_REQUEST("환불요청"),
    REFUND_SUCCESS("환불완료"),
    REFUND_REFUSE("환불요청거절");

    private String value;
}
