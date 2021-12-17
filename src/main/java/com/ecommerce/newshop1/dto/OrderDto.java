package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.Delivery;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.OrderItem;
import com.ecommerce.newshop1.entity.OrderPaymentInformation;
import com.ecommerce.newshop1.utils.enums.PayType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private Member member;
    private Delivery delivery;
    private List<OrderItem> orderItems = new ArrayList<>();
    private OrderPaymentInformation paymentInfo;
    private PayType payType;
    private int totalPrice;
    private String orderNum;
    private LocalDateTime createdDate;

}
