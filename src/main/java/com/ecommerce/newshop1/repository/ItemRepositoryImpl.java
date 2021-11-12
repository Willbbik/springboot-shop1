package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.entity.QItem;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.util.List;


public class ItemRepositoryImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Long searchTotal(SearchDto searchDto) {

        // test

        return queryFactory
                .select(QItem.item.id)
                .from(QItem.item)
                .where(eqItemName(searchDto.getItemName()),
                        eqCategory(searchDto.getCategory()),
                        eqSaleStatus(searchDto.getSaleStatus()))
                .fetchCount();
    }

//    @Override
//    public Long searchTotal(SearchDto searchDto) {
//
//            return queryFactory
//                .select(new QItemDto(
//                        QItem.item.id
//                ))
//                .from(QItem.item)
//                .where(eqItemName(searchDto.getItemName()),
//                        eqCategory(searchDto.getCategory()),
//                        eqSaleStatus(searchDto.getSaleStatus()))
//                .orderBy(QItem.item.createdDate.desc())
//                .fetchResults().getTotal();
//    }

    @Override
    public List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable) {
        // test

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


//    @Override
//    public List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable) {
//
//        QueryResults<ItemDto> result = queryFactory
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
//                .where(eqItemName(searchDto.getItemName()),
//                        eqCategory(searchDto.getCategory()),
//                        eqSaleStatus(searchDto.getSaleStatus()))
//                .orderBy(QItem.item.createdDate.desc())
//                .limit(pageable.getPageSize())
//                .offset(pageable.getOffset())
//                .fetchResults();
//
//        return result.getResults();
//    }



    private BooleanExpression eqCategory(String category){
        return category != null ? QItem.item.category.eq(category) : null;
    }

    private BooleanExpression eqItemName(String itemName){
        return itemName != null ? QItem.item.itemName.eq(itemName) : null;
    }

    private BooleanExpression eqSaleStatus(String saleStatus){
        return saleStatus != null ? QItem.item.saleStatus.eq(saleStatus) : null;
    }

}

