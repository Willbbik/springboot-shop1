package com.ecommerce.newshop1.dto;

import lombok.*;

import java.util.Date;

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
    private String productPrice;
    private Date createdDate;
    private Date modifiedDate;


}
