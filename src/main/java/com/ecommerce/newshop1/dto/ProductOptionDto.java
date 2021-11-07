package com.ecommerce.newshop1.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionDto {

    private Long id;
    private Long productId;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String option5;
    private String stock;


}
