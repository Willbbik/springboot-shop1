package com.ecommerce.newshop1.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {

    private Long id;
    private String subject;
    private String content;
    private String category;
    private String writer;
    private String hide;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


}
