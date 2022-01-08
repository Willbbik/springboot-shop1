package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.OrderDto;
import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.entity.*;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

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
                .where(eqDeliveryStatus(deliveryStatus))
                .orderBy(QOrderItem.orderItem.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<OrderItemDto> searchBySearchDtoAndDeliveryStatus(SearchDto searchDto, DeliveryStatus deliveryStatus, Pageable pageable) {

        return queryFactory
                .select(Projections.fields(OrderItemDto.class,
                        QOrderItem.orderItem.id,
                        QOrderItem.orderItem.order,
                        QOrderItem.orderItem.item,
                        QOrderItem.orderItem.totalPrice,
                        QOrderItem.orderItem.deliveryStatus
                ))
                .from(QOrderItem.orderItem)
                .where( eqSearchDto(searchDto),
                        eqDeliveryStatus(deliveryStatus))
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
    public Long searchTotalOrderItem(DeliveryStatus deliveryStatus, SearchDto searchDto) {

        return queryFactory
                .select(Projections.fields(OrderItemDto.class,
                            QOrderItem.orderItem.id))
                .from(QOrderItem.orderItem)
                .where(
                    eqDeliveryStatus(deliveryStatus),
                    eqCustomerName(searchDto.getCustomerName()),
                    eqOrderNum(searchDto.getOrderNum()),
                    eqItemName(searchDto.getItemName())
                )
                .fetchCount();
    }


    @Override
    public List<OrderItemDto> searchAllByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable, SearchDto searchDto) {

        List<Long> ids = queryFactory
                .select(QOrderItem.orderItem.id)
                .from(QOrderItem.orderItem)
                .where(
                    eqDeliveryStatus(deliveryStatus),
                    eqCustomerName(searchDto.getCustomerName()),
                    eqOrderNum(searchDto.getOrderNum()),
                    eqItemName(searchDto.getItemName())
                )
                .orderBy(QOrderItem.orderItem.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        if(CollectionUtils.isEmpty(ids)){
            return null;
        }

        return queryFactory.select(Projections.fields(OrderItemDto.class,
                    QOrderItem.orderItem.id,
                    QOrderItem.orderItem.order,
                    QOrderItem.orderItem.item,
                    QOrderItem.orderItem.totalPrice,
                    QOrderItem.orderItem.deliveryStatus,
                    QOrderItem.orderItem.quantity,
                    QOrderItem.orderItem.wayBillNum
                ))
                .from(QOrderItem.orderItem)
                .where(QOrderItem.orderItem.id.in(ids))
                .orderBy(QOrderItem.orderItem.id.desc())
                .fetch();
    }


    private BooleanExpression eqSearchDto(SearchDto searchDto){
        if(StringUtils.isBlank(searchDto.getKeyType())) return null;

        if (searchDto.getKeyType().equals("customerName")){    // 구매자
            return QDeliveryAddress.deliveryAddress.customerName.contains(searchDto.getKeyValue());
            // return QOrderItem.orderItem.order.delivery.deliveryAddress.customerName.contains(searchDto.getKeyValue());
        } else if(searchDto.getKeyType().equals("orderNum")){  // 주문번호
            return QOrderItem.orderItem.order.orderNum.contains(searchDto.getKeyValue());
        } else {
            return null;
        }
    }

    private BooleanExpression eqDeliveryStatus(DeliveryStatus deliveryStatus){
        if(deliveryStatus.equals(DeliveryStatus.EMPTY)) return null;
        return QOrderItem.orderItem.deliveryStatus.eq(deliveryStatus);
    }

    private BooleanExpression eqCustomerName(String customerName){
        if(StringUtils.isBlank(customerName)) return null;
        return QOrderItem.orderItem.order.delivery.deliveryAddress.customerName.eq(customerName);
    }

    private BooleanExpression eqOrderNum(String orderNum){
        if(StringUtils.isBlank(orderNum)) return null;
        return QOrderItem.orderItem.order.orderNum.eq(orderNum);
    }

    private BooleanExpression eqItemName(String itemName){
        if(StringUtils.isBlank(itemName)) return null;
        return QOrderItem.orderItem.item.itemName.eq(itemName);
    }


    private BooleanExpression ltOrderId(Long orderId){
        if(orderId == null){
            return null;
        }
        return QOrder.order.id.lt(orderId);
    }

}
