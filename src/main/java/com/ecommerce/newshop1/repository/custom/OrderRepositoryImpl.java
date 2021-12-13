package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.OrderDto;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.QOrder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OrderRepositoryImpl implements OrderRepositoryCustom{

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<OrderDto> searchAllByMember(Long orderId, Member member) {
            return queryFactory
                    .select(Projections.fields(OrderDto.class,
                            QOrder.order.id,
                            QOrder.order.member,
                            QOrder.order.delivery,
                            QOrder.order.paymentInfo,
                            QOrder.order.orderItems,
                            QOrder.order.orderNum,
                            QOrder.order.payType,
                            QOrder.order.totalPrice,
                            QOrder.order.createdDate
                    ))
                    .from(QOrder.order)
                    .where(
                            QOrder.order.member.eq(member),
                            ltOrderId(orderId)
                    )
                    .orderBy(QOrder.order.id.desc())
                    .limit(3)
                    .fetch();
    }


    private BooleanExpression ltOrderId(Long orderId){
        if(orderId == null){
            return null;
        }
        return QOrder.order.id.lt(orderId);
    }

}
