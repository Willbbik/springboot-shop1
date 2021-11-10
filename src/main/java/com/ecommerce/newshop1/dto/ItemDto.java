package com.ecommerce.newshop1.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ItemDto {

    private Long id;

    private String category;

    private String itemName;

    private String color;

    private String size;

    private int price;

    private String itemInfo;

    private String model;

    private String saleStatus;

    private String imageUrl;

    private Long itemIdx;

    private Date createdDate;

    private Date modifiedDate;


    @QueryProjection
    public ItemDto(Long id, String category, String itemName, String color, String size, int price, String itemInfo, String model, String saleStatus, String imageUrl, Long itemIdx, Date createdDate, Date modifiedDate) {
        this.id = id;
        this.category = category;
        this.itemName = itemName;
        this.color = color;
        this.size = size;
        this.price = price;
        this.itemInfo = itemInfo;
        this.model = model;
        this.saleStatus = saleStatus;
        this.imageUrl = imageUrl;
        this.itemIdx = itemIdx;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

}


