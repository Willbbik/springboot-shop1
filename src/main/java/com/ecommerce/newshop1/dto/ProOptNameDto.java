package com.ecommerce.newshop1.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProOptNameDto {

    private Long id;
    private Long productId;
    private String optionName1;
    private String optionName2;
    private String optionName3;
    private String optionName4;
    private String optionName5;

}
