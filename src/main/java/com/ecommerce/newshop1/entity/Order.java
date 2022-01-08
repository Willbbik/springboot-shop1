package com.ecommerce.newshop1.entity;

import com.ecommerce.newshop1.enums.DeliveryStatus;
import com.ecommerce.newshop1.enums.PayType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
    private Delivery delivery;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
    private OrderPaymentInformation paymentInfo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PayType payType;

    @ColumnDefault("0")
    private int totalPrice;

    @Column(name = "order_num", nullable = false)
    private String orderNum;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime depositDate;

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    public void setPaymentInfo(OrderPaymentInformation paymentInfo){
        this.paymentInfo = paymentInfo;
        paymentInfo.setOrder(this);
    }

    public void setOrderItems(OrderItem orderItem){
        this.getOrderItems().add(orderItem);
        orderItem.setOrder(this);
    }

    public void setMember(Member member){
        member.getOrderList().add(this);
        this.member = member;
    }

    public static Order createOrder(Member member, Delivery delivery, List<OrderItem> orderItems, PayType payType, OrderPaymentInformation paymentInfo, String orderId){

        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        order.setPaymentInfo(paymentInfo);

        for(OrderItem orderItem : orderItems){
            if(payType.equals(PayType.KAKAO_PAY)) orderItem.setDeliveryStatus(DeliveryStatus.DEPOSIT_SUCCESS);
            order.setOrderItems(orderItem);
        }

        order.setPayType(payType);
        order.setOrderNum(orderId);
        order.setTotalPrice(getOrderTotalPrice(orderItems));
        return order;
    }

    public static int getOrderTotalPrice(List<OrderItem> orderItems){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
