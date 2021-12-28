package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private Long id;
    private Item item;
    private Member member;
    private String writer;
    private String content;

}
