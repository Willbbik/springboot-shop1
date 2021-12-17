package com.ecommerce.newshop1.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.Generated;

/**
 * com.ecommerce.newshop1.dto.QItemDto is a Querydsl Projection type for ItemDto
 */
@Generated("com.querydsl.codegen.ProjectionSerializer")
public class QItemDto extends ConstructorExpression<ItemDto> {

    private static final long serialVersionUID = -1636011387L;

    public QItemDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> category, com.querydsl.core.types.Expression<String> itemName, com.querydsl.core.types.Expression<String> itemCode, com.querydsl.core.types.Expression<String> color, com.querydsl.core.types.Expression<String> size, com.querydsl.core.types.Expression<Integer> price, com.querydsl.core.types.Expression<String> itemInfo, com.querydsl.core.types.Expression<String> model, com.querydsl.core.types.Expression<String> saleStatus, com.querydsl.core.types.Expression<String> imageUrl, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdDate, com.querydsl.core.types.Expression<java.time.LocalDateTime> modifiedDate) {
        super(ItemDto.class, new Class<?>[]{long.class, String.class, String.class, String.class, String.class, String.class, int.class, String.class, String.class, String.class, String.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, id, category, itemName, itemCode, color, size, price, itemInfo, model, saleStatus, imageUrl, createdDate, modifiedDate);
    }

    public QItemDto(com.querydsl.core.types.Expression<Long> id) {
        super(ItemDto.class, new Class<?>[]{long.class}, id);
    }

}

