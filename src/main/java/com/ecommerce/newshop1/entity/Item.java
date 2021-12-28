package com.ecommerce.newshop1.entity;

import lombok.*;

import javax.persistence.*;
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
    @Column(name = "item_id")
    private Long id;

    @Column
    private String category;

    @Column
    private String itemName;

    @Column
    private String itemCode;

    @Column
    private String color;

    @Column
    private String size;

    @Column
    private int price;

    @Column
    private String itemInfo;

    @Column
    private String model;

    @Column
    private String saleStatus;

    @Column
    private String imageUrl;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "item")
    private List<CartItem> cartItemList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "item")
    private List<ItemImage> itemImageList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "item")
    private List<QnAEntity> qnAEntityList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "item")
    private List<Review> reviewList = new ArrayList<>();

    public void setItemImageList(ItemImage itemImage){
        itemImageList.add(itemImage);
        itemImage.setItem(this);
    }

    public void setQnaEntityList(QnAEntity qnAEntity){
        qnAEntityList.add(qnAEntity);
        qnAEntity.setItem(this);
    }

    public void addReviewList(Review review){
        this.getReviewList().add(review);
        review.setItem(this);
    }

}
