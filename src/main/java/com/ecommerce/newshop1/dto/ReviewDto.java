package com.ecommerce.newshop1.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private Long id;
    private String writer;
    private String content;
    private int cm;

}
