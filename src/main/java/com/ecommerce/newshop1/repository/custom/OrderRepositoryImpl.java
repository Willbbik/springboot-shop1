package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.OrderDto;
import com.ecommerce.newshop1.entity.*;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Pageable;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepositoryCustom{

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<Order> searchAllByMember(Long lastOrderId, Member member) {
            return queryFactory
                    .selectFrom(QOrder.order)
                    .where(
                            QOrder.order.member.eq(member),
                            ltOrderId(lastOrderId)
                    )
                    .orderBy(QOrder.order.id.desc())
                    .limit(3)
                    .fetch();
    }


    @Override
    public List<OrderDto> searchByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable) {
        return queryFactory
                .select(Projections.fields(OrderDto.class,
                        QOrder.order.id,
                        QOrder.order.totalPrice,
                        QOrder.order.depositDate
                ))
                .from(QOrder.order)
                .where(QOrder.order.delivery.deliveryStatus.eq(deliveryStatus))
                .orderBy(QOrder.order.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }


    private BooleanExpression ltOrderId(Long orderId){
        if(orderId == null){
            return null;
        }
        return QOrder.order.id.lt(orderId);
    }

}
