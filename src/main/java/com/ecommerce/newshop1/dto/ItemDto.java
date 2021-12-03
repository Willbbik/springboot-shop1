package com.ecommerce.newshop1.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;

    private String category;

    private String itemName;

    private String itemCode;

    private String color;

    private String size;

    private int price;

    private String itemInfo;

    private String model;

    private String saleStatus;

    private String imageUrl;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private int quantity;
    private int totalPrice;

    @QueryProjection
    public ItemDto(Long id, String category, String itemName, String itemCode, String color, String size, int price, String itemInfo, String model, String saleStatus, String imageUrl, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.category = category;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.color = color;
        this.size = size;
        this.price = price;
        this.itemInfo = itemInfo;
        this.model = model;
        this.saleStatus = saleStatus;
        this.imageUrl = imageUrl;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    @QueryProjection
    public ItemDto(Long id) {
        this.id = id;
    }
}


