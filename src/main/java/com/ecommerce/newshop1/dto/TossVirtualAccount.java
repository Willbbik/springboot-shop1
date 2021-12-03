package com.ecommerce.newshop1.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TossVirtualAccount {

    private String accountNumber;
    private String accountType;
    private String bank;
    private String customerName;
    private String dueDate;
    private String refundStatus;
    private boolean expired;
    private String settlementStatus;
    private String secret;


}
