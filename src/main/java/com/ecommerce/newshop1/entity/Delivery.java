package com.ecommerce.newshop1.entity;

import com.ecommerce.newshop1.enums.DeliveryStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery")
public class Delivery extends TimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "delivery_address_id")
    private DeliveryAddress deliveryAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(nullable = false)
    private String orderName;

    public void setDeliveryAddress(DeliveryAddress deliveryAddress){
        this.deliveryAddress = deliveryAddress;
        deliveryAddress.setDelivery(this);
    }


}
