package com.ecommerce.newshop1.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;

    private String category;

    private String productName;

    private int price;

    private String productInfo;

    private String model;

}
