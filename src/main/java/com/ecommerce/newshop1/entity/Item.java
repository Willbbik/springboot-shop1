package com.ecommerce.newshop1.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "item", indexes = @Index(name = "idx_item", columnList = "itemName, category, saleStatus"))
public class Item extends TimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    private String itemName;

    private String itemCode;

    private String color;

    private String size;

    private int price;

    private String itemInfo;

    private String model;

    private String saleStatus;

    private String imageUrl;


    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }


}
