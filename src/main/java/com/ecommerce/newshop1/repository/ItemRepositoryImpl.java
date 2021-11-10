package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.QItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.entity.QItem;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;


public class ItemRepositoryImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ItemDto> searchAll(SearchDto searchDto, Pageable pageable) {

        QueryResults<ItemDto> result = queryFactory
                .select(new QItemDto(
                        QItem.item.id,
                        QItem.item.category,
                        QItem.item.itemName,
                        QItem.item.color,
                        QItem.item.size,
                        QItem.item.price,
                        QItem.item.itemInfo,
                        QItem.item.model,
                        QItem.item.saleStatus,
                        QItem.item.imageUrl,
                        QItem.item.itemIdx,
                        QItem.item.createdDate,
                        QItem.item.modifiedDate
                ))
                .from(QItem.item)
//                .where(eqCategory(searchDto.getCategory()),
//                        eqItemName(searchDto.getItemName()),
//                        eqSaleStatus(searchDto.getSaleStatus()))
                .orderBy(QItem.item.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchResults();

        List<ItemDto> itemDtos = result.getResults();
        Long total = result.getTotal();

        return new PageImpl<>(itemDtos, pageable, total);

    }

    @Override
    public Long searchMaxItemIdx() {
        return null;
    }

    //    @Override
//    public List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable) {
//
//        List<ItemDto> itemDtos = queryFactory
//                .select(new QItemDto(
//                        QItem.item.id,
//                        QItem.item.category,
//                        QItem.item.itemName,
//                        QItem.item.color,
//                        QItem.item.size,
//                        QItem.item.price,
//                        QItem.item.itemInfo,
//                        QItem.item.model,
//                        QItem.item.saleStatus,
//                        QItem.item.imageUrl,
//                        QItem.item.createdDate,
//                        QItem.item.modifiedDate
//                ))
//                .from(QItem.item)
////                .where(eqCategory(searchDto.getCategory()),
////                        eqItemName(searchDto.getItemName()),
////                        eqSaleStatus(searchDto.getSaleStatus()))
//                .orderBy(QItem.item.id.desc())
//                .limit(pageable.getPageSize())
//                .offset(pageable.getOffset())
//                .fetch();
//
//        return itemDtos;
//
//    }

    private BooleanExpression eqCategory(String category){
        if(category == null){
            return null;
        }
        return QItem.item.category.eq(category);
    }

    private BooleanExpression eqItemName(String itemName){
        if(itemName == null){
            return null;
        }
        return QItem.item.itemName.eq(itemName);
    }

    private BooleanExpression eqSaleStatus(String saleStatus){
        if(saleStatus == null){
            return null;
        }
        return QItem.item.saleStatus.eq(saleStatus);
    }

}

