package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.Item;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImageDto {

    private Long id;

    private Item item;

    private String imageUrl;

    private String imageName;

}
