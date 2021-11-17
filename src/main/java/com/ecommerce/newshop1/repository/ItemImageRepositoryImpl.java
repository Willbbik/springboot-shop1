package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.dto.ItemImageDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.QItemImage;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ItemImageRepositoryImpl implements ItemImageRepositoryCustom {


    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<ItemImageDto> searchAll(Item item) {

        return queryFactory
                .select(Projections.fields(ItemImageDto.class,
                    QItemImage.itemImage.id,
                    QItemImage.itemImage.imageUrl,
                    QItemImage.itemImage.imageName
                    ))
                .from(QItemImage.itemImage)
                .where(eqItemId(item))
                .fetch();
    }


    private BooleanExpression eqItemId(Item item){
        return QItemImage.itemImage.itemId.eq(item);
    }



}
