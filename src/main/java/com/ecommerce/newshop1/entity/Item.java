package com.ecommerce.newshop1.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "item")
    private List<CartItem> cartItemList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "item")
    private List<ItemImage> itemImageList = new ArrayList<>();

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "item")
//    private List<QnAEntity> qnAEntityList = new ArrayList<>();

    public void setItemImageList(ItemImage itemImage){

        itemImageList.add(itemImage);
        itemImage.setItem(this);
    }


}
