package com.ecommerce.newshop1.entity;

import com.ecommerce.newshop1.utils.enums.DepositStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "orders")
    private Order order;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_address")
    private DeliveryAddress deliveryAddress;

    @Enumerated(EnumType.STRING)
    private DepositStatus depositStatus;

    public void setDeliveryAddress(DeliveryAddress deliveryAddress){
        this.deliveryAddress = deliveryAddress;
        deliveryAddress.setDelivery(this);
    }


}
