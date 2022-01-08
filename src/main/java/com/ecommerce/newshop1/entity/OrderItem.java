package com.ecommerce.newshop1.entity;

import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column
    private String wayBillNum;

    @Column
    private int quantity;

    @Column
    private int totalPrice;

    public static List<OrderItemDto> toDtoList(List<OrderItem> orderItems){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);

        List<OrderItemDto> orderItemDtoList = new ArrayList<>();

        for(OrderItem orderItem : orderItems){
            orderItemDtoList.add(mapper.map(orderItem, OrderItemDto.class));
        }
        return orderItemDtoList;
    }

}
