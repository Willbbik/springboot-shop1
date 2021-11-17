package com.ecommerce.newshop1.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "item_image", indexes = @Index(name = "itemId_idx", columnList = "item_id"))
public class ItemImage extends TimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item itemId;

    private String imageUrl;

    private String imageName;


}
