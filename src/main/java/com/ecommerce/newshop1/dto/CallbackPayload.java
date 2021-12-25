package com.ecommerce.newshop1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallbackPayload {

    private String secret;
    private String status;
    private String orderId;

}
