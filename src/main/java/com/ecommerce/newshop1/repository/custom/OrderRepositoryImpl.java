package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.OrderDto;
import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.Order;
import com.ecommerce.newshop1.entity.QOrder;
import com.ecommerce.newshop1.entity.QOrderItem;
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
    public List<Order> searchAllByMember(Long orderId, Member member) {
            return queryFactory
                    .select(QOrder.order)
                    .from(QOrder.order)
                    .where(
                            QOrder.order.member.eq(member),
                            ltOrderId(orderId)
                    )
                    .orderBy(QOrder.order.id.desc())
                    .limit(3)
                    .fetch();
    }

    @Override
    public List<OrderItemDto> searchByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable) {
        return queryFactory
                .select(Projections.fields(OrderItemDto.class,
                        QOrderItem.orderItem.order,
                        QOrderItem.orderItem.item,
                        QOrderItem.orderItem.totalPrice,
                        QOrderItem.orderItem.deliveryStatus
                        ))
                .from(QOrderItem.orderItem)
                .where(QOrderItem.orderItem.deliveryStatus.eq(deliveryStatus))
                .orderBy(QOrderItem.orderItem.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<OrderDto> searchByDepositSuccess(DeliveryStatus deliveryStatus, Pageable pageable) {
        return queryFactory
                .select(Projections.fields(OrderDto.class,
                        QOrder.order.delivery,
                        QOrder.order.totalPrice,
                        QOrder.order.depositDate
                        ))
                .from(QOrder.order)
                .where(QOrder.order.delivery.deliveryStatus.eq(deliveryStatus))
                .orderBy(QOrder.order.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<OrderItemDto> searchByDeliveryStatus(DeliveryStatus deliveryStatus) {

            return queryFactory
                    .select(Projections.fields(OrderItemDto.class,
                            QOrderItem.orderItem.order,
                            QOrderItem.orderItem.item,
                            QOrderItem.orderItem.totalPrice,
                            QOrderItem.orderItem.deliveryStatus
                            ))
                    .from(QOrderItem.orderItem)
                    .where(QOrderItem.orderItem.deliveryStatus.eq(deliveryStatus))
                    .orderBy(QOrderItem.orderItem.id.desc())
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
