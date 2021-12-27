package com.ecommerce.newshop1.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = 939620422L;

    public static final QItem item = new QItem("item");

    public final QTimeEntity _super = new QTimeEntity(this);

    public final ListPath<CartItem, QCartItem> cartItemList = this.<CartItem, QCartItem>createList("cartItemList", CartItem.class, QCartItem.class, PathInits.DIRECT2);

    public final StringPath category = createString("category");

    public final StringPath color = createString("color");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath itemCode = createString("itemCode");

    public final ListPath<ItemImage, QItemImage> itemImageList = this.<ItemImage, QItemImage>createList("itemImageList", ItemImage.class, QItemImage.class, PathInits.DIRECT2);

    public final StringPath itemInfo = createString("itemInfo");

    public final StringPath itemName = createString("itemName");

    public final StringPath model = createString("model");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final ListPath<QnAEntity, QQnAEntity> qnAEntityList = this.<QnAEntity, QQnAEntity>createList("qnAEntityList", QnAEntity.class, QQnAEntity.class, PathInits.DIRECT2);

    public final ListPath<Review, QReview> reviewList = this.<Review, QReview>createList("reviewList", Review.class, QReview.class, PathInits.DIRECT2);

    public final StringPath saleStatus = createString("saleStatus");

    public final StringPath size = createString("size");

    public QItem(String variable) {
        super(Item.class, forVariable(variable));
    }

    public QItem(Path<? extends Item> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItem(PathMetadata metadata) {
        super(Item.class, metadata);
    }

}

