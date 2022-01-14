package com.ecommerce.newshop1.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemQnA is a Querydsl query type for ItemQnA
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QItemQnA extends EntityPathBase<ItemQnA> {

    private static final long serialVersionUID = 1930205086L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemQnA itemQnA = new QItemQnA("itemQnA");

    public final QTimeEntity _super = new QTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath hide = createString("hide");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItem item;

    public final QItemQnAReply itemQnAReply;

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final BooleanPath replyEmpty = createBoolean("replyEmpty");

    public final StringPath title = createString("title");

    public final StringPath writer = createString("writer");

    public QItemQnA(String variable) {
        this(ItemQnA.class, forVariable(variable), INITS);
    }

    public QItemQnA(Path<? extends ItemQnA> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemQnA(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemQnA(PathMetadata metadata, PathInits inits) {
        this(ItemQnA.class, metadata, inits);
    }

    public QItemQnA(Class<? extends ItemQnA> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item")) : null;
        this.itemQnAReply = inits.isInitialized("itemQnAReply") ? new QItemQnAReply(forProperty("itemQnAReply"), inits.get("itemQnAReply")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

