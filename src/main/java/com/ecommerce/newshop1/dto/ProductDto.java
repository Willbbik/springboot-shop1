package com.ecommerce.newshop1.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String pageName;
    private String productName;
    private int productPrice;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


}
