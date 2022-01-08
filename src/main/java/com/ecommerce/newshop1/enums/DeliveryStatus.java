package com.ecommerce.newshop1.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {

    DEPOSIT_READY("DEPOSIT_READY", "입금대기"),
    DEPOSIT_PRICE_LACK("DEPOSIT_PRICE_LACK", "입금금액부족"),
    DEPOSIT_SUCCESS("DEPOSIT_SUCCESS", "입금완료"),
    DELIVERY_READY("DELIVERY_READY", "배송준비중"),
    DELIVERY_ING("DELIVERY_ING", "배송중"),
    DELIVERY_COMPLETION("DELIVERY_COMPLETION", "배송완료"),
    ORDER_CANCEL("ORDER_CANCEL", "주문취소"),
    EMPTY("EMPTY", "없음");

    private String title;
    private String value;

    public static DeliveryStatus findByDeliveryStatus(String deliveryStatus){
        return Arrays.stream(DeliveryStatus.values())
                .filter(d -> d.getTitle().equals(deliveryStatus))
                .findAny()
                .orElse(EMPTY);
    }


}
