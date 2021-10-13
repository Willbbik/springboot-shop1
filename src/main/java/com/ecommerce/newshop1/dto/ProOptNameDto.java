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
    private String option1Name;
    private String option2Name;
    private String option3Name;
    private String option4Name;
    private String option5Name;

}
