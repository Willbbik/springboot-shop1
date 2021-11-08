package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.Item;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ItemImageDto {

    private Long id;

    private Item item;

    private String imageUrl;

    private String imageName;

    @QueryProjection
    public ItemImageDto(Long id, Item item, String imageUrl, String imageName) {
        this.id = id;
        this.item = item;
        this.imageUrl = imageUrl;
        this.imageName = imageName;
    }
}
