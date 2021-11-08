package com.ecommerce.newshop1.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    private String itemName;

    private String color;

    private String size;

    private int price;

    private String itemInfo;

    private String model;

    private String saleStatus;

    private String imageUrl;

    @CreatedDate
    @Column(nullable = false)
    private Date createdDate;

    @LastModifiedDate
    private Date modifiedDate;

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

}
