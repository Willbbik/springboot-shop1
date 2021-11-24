package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.QItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.entity.QItem;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.util.List;


public class ItemRepositoryImpl implements ItemRepositoryCustom{

    @Autowired
    private JPAQueryFactory queryFactory;

    public Long searchTotal(SearchDto searchDto) {

            return queryFactory
                .select(new QItemDto(
                        QItem.item.id
                ))
                .from(QItem.item)
                .where(eqItemName(searchDto.getItemName()),
                        eqCategory(searchDto.getCategory()),
                        eqSaleStatus(searchDto.getSaleStatus()))
                .fetchCount();
    }

    public List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable) {
        // 커버링 인덱스로 대상 조회
        List<Long> ids = queryFactory
                .select(QItem.item.id)
                .from(QItem.item)
                .where(eqItemName(searchDto.getItemName()),
                        eqCategory(searchDto.getCategory()),
                        eqSaleStatus(searchDto.getSaleStatus()))
                .orderBy(QItem.item.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        if(CollectionUtils.isEmpty(ids)){
            return null;
        }

        return queryFactory
                .select(Projections.fields(ItemDto.class,
                        QItem.item.id,
                        QItem.item.itemName,
                        QItem.item.category,
                        QItem.item.saleStatus,
                        QItem.item.color,
                        QItem.item.size,
                        QItem.item.price,
                        QItem.item.imageUrl,
                        QItem.item.createdDate
                ))
                .from(QItem.item)
                .where(QItem.item.id.in(ids))
                .orderBy(QItem.item.id.desc())
                .fetch();
    }


    public List<ItemDto> searchAllNoOffset(Long itemId){
            return queryFactory
                    .select(Projections.fields(ItemDto.class,
                            QItem.item.id,
                            QItem.item.itemName,
                            QItem.item.price,
                            QItem.item.imageUrl
                            ))
                    .from(QItem.item)
                    .where(ltItemId(itemId),
                            eqSaleStatus("onsale"))
                    .orderBy(QItem.item.id.desc())
                    .limit(12)
                    .fetch();
    }


    private BooleanExpression ltItemId(Long itemId){
        if(itemId == null){
            return null;
        }
        return QItem.item.id.lt(itemId);
    }


    private BooleanExpression eqCategory(String category){
        if(StringUtils.isBlank(category) || category.equals("whole")){
            return null;
        }

        return QItem.item.category.eq(category);
    }

    private BooleanExpression eqItemName(String itemName){
        if(StringUtils.isBlank(itemName)){
            return null;
        }

        return QItem.item.itemName.contains(itemName);
    }

    private BooleanExpression eqSaleStatus(String saleStatus){
        if(StringUtils.isBlank(saleStatus)){
            return null;
        }

        return QItem.item.saleStatus.eq(saleStatus);
    }

}

