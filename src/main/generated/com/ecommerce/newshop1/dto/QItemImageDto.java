package com.ecommerce.newshop1.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.Generated;

/**
 * com.ecommerce.newshop1.dto.QItemImageDto is a Querydsl Projection type for ItemImageDto
 */
@Generated("com.querydsl.codegen.ProjectionSerializer")
public class QItemImageDto extends ConstructorExpression<ItemImageDto> {

    private static final long serialVersionUID = -41931362L;

    public QItemImageDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<? extends com.ecommerce.newshop1.entity.Item> item, com.querydsl.core.types.Expression<String> imageUrl, com.querydsl.core.types.Expression<String> imageName) {
        super(ItemImageDto.class, new Class<?>[]{long.class, com.ecommerce.newshop1.entity.Item.class, String.class, String.class}, id, item, imageUrl, imageName);
    }

}

